package phantom.tool.validate.annotation;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.collect.ImmutableMap;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import phantom.annotation.Description;
import phantom.common.UString;
import phantom.reflect.UReflect;
import phantom.reflect.UType;
import phantom.tool.validate.Validate;

/**
 * 枚举类型映射注解 <br>
 * <strong>对枚举的要求:<br>
 * 1.枚举必须继承IMapping&lt;V&gt;接口。类型参数V指定了可以映射到枚举上的类型。详情见IMapping接口。<br>
 * 2.枚举必须实现一个public static Enum of(V
 * value)方法。V的类型为基本类型时,可以为对应装箱类,反之亦然;其他类型时必须严格对应。<br>
 * 3.方法名由MappingHelper.VALIDATE_METHOD指定。该方法必须在映射成功时返回对应枚举,失败时返回null。<br>
 * 4.枚举应拥有一个public static final V
 * defaultValue字段。字段用于指定默认值。如果字段值为null或者无该字段,默认值被指定为null。<br>
 * 5.该字段名由MappingHelper.DEFAULT_VALUE_FIELD指定,类型同IMapping的类型参数V。<br>
 * </strong><br>
 * 注解使用范例:<br>
 * <code>
 * &#64;MapEnum(UserStatusEnum.class)<br>
 * private Byte status;
 * </code><br>
 * 以下为注解参数说明:<br>
 * message: String类型,代表验证失败时的返回信息<br>
 * type: Class类型,代表对应的枚举类.<br>
 * <strong>将枚举类型同相应映射类型的对象进行映射,并可以验证正确性。<br>
 * 对枚举类型相应的映射类型的参数用本注解标注,可以自动生成swagger文档。作用类似于@ApiParam。</strong><br>
 * @see phantom.tool.validate.annotation.Mapping.IMapping
 * @see phantom.tool.validate.annotation.Mapping.MappingHelper
 * @author Frodez
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Mapping.Validator.class)
public @interface Mapping {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 适用的枚举类
	 * @author Frodez
	 */
	Class<? extends Enum<?>> value();

	/**
	 * 实现Mapping功能继承接口。只用于枚举上,且枚举如果不继承该接口,将无法通过检查,从而使用Mapping功能。<br>
	 * 示例代码如下:<br>
	 *
	 * <pre>
	 * &#64;Getter
	 * &#64;AllArgsConstructor
	 * public enum Enum implements IMapping&lt;Byte&gt; {
	 *
	 * 	YES((byte) 1, "YES"),
	 *
	 * 	NO((byte) 2, "NO");
	 *
	 * 	private Byte val;
	 *
	 * 	private String desc;
	 *
	 * 	private static final Map<Byte, Enum> enumMap = MappingHelper.generateMap(Enum.class);
	 *
	 * 	public static final Byte defaultValue = YES.val;
	 *
	 * 	public static Enum of(Byte value) {
	 * 		return enumMap.get(value);
	 * 	}
	 *
	 * }
	 * </pre>
	 *
	 * @author Frodez
	 */
	public interface IMapping<V> {

		/**
		 * 获取值
		 * @author Frodez
		 */
		V getVal();

		/**
		 * 获取描述
		 * @author Frodez
		 */
		String getDesc();

	}

	/**
	 * MappingHelper工具类,仅供Mapping相关功能使用!!!
	 * @author Frodez
	 */
	@UtilityClass
	public static class MappingHelper {

		/**
		 * 验证用方法名
		 */
		public static final String VALIDATE_METHOD = "of";

		/**
		 * 默认值字段
		 */
		public static final String DEFAULT_VALUE_FIELD = "defaultValue";

		private static final Map<Class<? extends Enum<?>>, MappingInfo> CACHE = new HashMap<>();

		private static MappingInfo get(Class<? extends Enum<?>> klass) {
			MappingInfo info = CACHE.get(klass);
			if (info == null) {
				info = generateAndCheck(klass);
				CACHE.put(klass, info);
			}
			return info;
		}

		/**
		 * 验证使用方法是否正确,用于代码检查
		 * @param valueType Mapping所在的字段类型或者参数类型
		 * @param enumType Mapping中的枚举类型
		 * @author Frodez
		 */
		public static void isLegal(Class<?> valueType, Class<? extends Enum<?>> enumType) {
			Class<?> paramType = get(enumType).method.type().parameterType(0);
			if (UType.isBoxType(paramType)) {
				if (!UType.isSameBoxType(paramType, valueType)) {
					throw new IllegalArgumentException("字段类型和验证方法参数类型不符!");
				}
			} else if (!paramType.isAssignableFrom(valueType)) {
				throw new IllegalArgumentException("字段类型和验证方法参数类型不符!");
			}
		}

		/**
		 * 获取枚举名称
		 * @author Frodez
		 */
		public static String getName(Class<? extends Enum<?>> klass) {
			return get(klass).name;
		}

		/**
		 * 获取验证方法
		 * @author Frodez
		 */
		public static MethodHandle getMethod(Class<? extends Enum<?>> klass) {
			return get(klass).method;
		}

		/**
		 * 获取错误信息
		 * @author Frodez
		 */
		public static String getErrorMessage(Class<? extends Enum<?>> klass) {
			return get(klass).errorMessage;
		}

		/**
		 * 获取所有允许的枚举值
		 * @author Frodez
		 */
		public static List<IMapping<?>> getEnums(Class<? extends Enum<?>> klass) {
			return get(klass).enums;
		}

		/**
		 * 获取默认的枚举值
		 * @author Frodez
		 */
		public static Object getDefaultValue(Class<? extends Enum<?>> klass) {
			return get(klass).defaultValue;
		}

		/**
		 * 枚举映射相关信息
		 * @author Frodez
		 */
		private static class MappingInfo {

			/**
			 * 名称
			 */
			public String name;

			/**
			 * 验证方法
			 */
			public MethodHandle method;

			/**
			 * 错误信息
			 */
			public String errorMessage;

			/**
			 * 所有允许的枚举值
			 */
			public List<IMapping<?>> enums;

			/**
			 * 默认的枚举值
			 */
			public Object defaultValue;

		}

		/**
		 * 生成枚举映射相关信息,同时实施检查
		 * @author Frodez
		 */
		@SneakyThrows
		private static MappingInfo generateAndCheck(Class<? extends Enum<?>> klass) {
			MappingInfo info = new MappingInfo();
			Description description = klass.getAnnotation(Description.class);
			if (description != null) {
				info.name = description.name();
			}
			// 解析类型
			ResolvedType imappingType = Stream.of(klass.getGenericInterfaces()).map((type) -> UType.resolve(type))
					.filter((type) -> {
						return type.getErasedType() == IMapping.class;
					}).findAny()
					.orElseThrow(() -> new RuntimeException("该枚举未继承" + IMapping.class.getCanonicalName() + "接口"));
			Class<?> valueType = imappingType.getTypeParameters().get(0).getErasedType();
			// info.method
			Method validateMethod = Stream.of(klass.getDeclaredMethods()).filter((method) -> {
				if (!VALIDATE_METHOD.equals(method.getName())) {
					return false;
				}
				if (!Modifier.isStatic(method.getModifiers())) {
					return false;
				}
				if (method.getReturnType() != klass) {
					return false;
				}
				Class<?>[] parameters = method.getParameterTypes();
				if (parameters.length != 1) {
					return false;
				}
				return UType.isBoxType(valueType) ? UType.isSameBoxType(valueType, parameters[0])
						: valueType == parameters[0];
			}).findAny().orElseThrow(() -> new IllegalArgumentException("未找到符合要求的验证方法!"));
			info.method = UReflect.LOOKUP.unreflect(validateMethod);
			// info.defaultValue
			try {
				Object defaultValue = UReflect.get(klass, DEFAULT_VALUE_FIELD, null);
				if (defaultValue != null && defaultValue.getClass() != valueType) {
					throw new IllegalArgumentException("默认值类型和" + IMapping.class.getCanonicalName() + "的类型参数不一致");
				}
				info.defaultValue = defaultValue;
			} catch (Throwable e) {
				e.printStackTrace();
				if (e instanceof NoSuchFieldException) {
					info.defaultValue = null;
				}
			}
			// info.enums
			Method method = klass.getDeclaredMethod("values");
			IMapping<?>[] enums = (IMapping<?>[]) UReflect.LOOKUP.unreflect(method).invoke();
			info.enums = Arrays.asList(enums);
			// info.errorMessage
			String introduction = String.join(", ",
					Stream.of(enums).map((item) -> UString.concat(item.getVal().toString(), ":", item.getDesc()))
							.collect(Collectors.toList()));
			if (info.defaultValue == null) {
				info.errorMessage = UString.concat("有效值为", introduction);
			} else {
				info.errorMessage = UString.concat("有效值为", introduction, ",默认值为", info.defaultValue.toString());
			}
			return info;
		}

		/**
		 * 为继承了IMapping接口的枚举生成Map
		 * @author Frodez
		 */
		@SneakyThrows
		@SuppressWarnings("unchecked")
		public static <V, E> Map<V, E> generateMap(Class<E> klass) {
			if (!klass.isEnum()) {
				throw new IllegalArgumentException("klass must be a enum!");
			}
			MappingInfo info = get((Class<? extends Enum<?>>) klass);
			var builder = ImmutableMap.<V, E>builder();
			for (IMapping<?> iter : info.enums) {
				E e = (E) iter;
				builder.put((V) iter.getVal(), e);
			}
			return builder.build();
		}

	}

	/**
	 * 枚举验证器
	 * @author Frodez
	 */
	class Validator implements ConstraintValidator<Mapping, Object> {

		/**
		 * 枚举类
		 */
		private Class<? extends Enum<?>> klass;

		/**
		 * 根据注解信息初始化验证器
		 * @author Frodez
		 */
		@Override
		public void initialize(Mapping enumValue) {
			klass = enumValue.value();
		}

		/**
		 * 验证
		 * @author Frodez
		 */
		@Override
		@SneakyThrows
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			if (value == null) {
				// 对于非空检查的情况,请继续使用@NotNull注解
				return true;
			}
			MappingHelper.MappingInfo info = MappingHelper.get(klass);
			if (info.method.invoke(value) != null) {
				return true;
			}
			Validate.changeMessage(context, info.errorMessage);
			return false;
		}

	}

}
