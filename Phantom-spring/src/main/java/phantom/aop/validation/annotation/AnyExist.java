package phantom.aop.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import phantom.common.UEmpty;
import phantom.common.UString;
import phantom.tool.validate.Validate;
import phantom.util.reflect.UReflect;

/**
 * 判断该类实体的指定字段是否至少有一个不为null<br>
 * 可以在实体上添加多个本注解<br>
 * 使用方式:<br>
 * 1.
 *
 * <pre>
 * &#64;AnyExist({ "a", "b" })
 * &#64;AnyExist({ "c", "d", "e" })
 * &#64;AnyExist({ "d", "e" })
 * </pre>
 *
 * 2.
 *
 * <pre>
 * &#64;AnyExists({ &#64;AnyExist({ "a", "b" }), &#64;AnyExist({ "c", "d", "e" }), &#64;AnyExist({ "d", "e" }) })
 * </pre>
 *
 * @see phantom.aop.validation.annotation.AnyExists
 * @author Frodez
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AnyExists.class)
@Constraint(validatedBy = AnyExist.Validator.class)
public @interface AnyExist {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 指定的字段名,不设置的话则默认为所有字段
	 * @author Frodez
	 */
	String[] value() default {};

	/**
	 * AnyExistHelper工具类,仅供AnyExist相关功能使用!!!
	 * @author Frodez
	 */
	@UtilityClass
	public static class AnyExistHelper {

		private static final Map<AnyExist, AnyExistInfo> CACHE = new ConcurrentHashMap<>();

		private AnyExistInfo get(Class<?> klass, AnyExist annotation) {
			return CACHE.computeIfAbsent(annotation, (key) -> generate(klass, key));
		}

		@SneakyThrows
		private static AnyExistInfo generate(Class<?> klass, AnyExist annotation) {
			AnyExistInfo result = new AnyExistInfo();
			String[] fieldNames = annotation.value();
			if (UEmpty.yes(fieldNames)) {
				result.fields = klass.getDeclaredFields();
				result.errorMessage = UString.concat(klass.getSimpleName(), "需要至少有一个字段不为null");
			} else {
				Field[] fields = new Field[fieldNames.length];
				for (int i = 0; i < fieldNames.length; i++) {
					String fieldName = fieldNames[i];
					fields[i] = klass.getDeclaredField(fieldName);
				}
				result.fields = fields;
				result.errorMessage = UString.concat(klass.getSimpleName(), "的", String.join(", ", fieldNames), "字段需要至少有一个不为null");
			}
			return result;
		}

		private static class AnyExistInfo {

			public Field[] fields;

			public String errorMessage;
		}

	}

	class Validator implements ConstraintValidator<AnyExist, Object> {

		private AnyExist annotation;

		@Override
		public void initialize(AnyExist annotation) {
			this.annotation = annotation;
		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			if (value == null) {
				return true;
			}
			AnyExistHelper.AnyExistInfo info = AnyExistHelper.get(value.getClass(), annotation);
			for (Field field : info.fields) {
				if (UReflect.get(field, value) != null) {
					return true;
				}
			}
			Validate.changeMessage(context, info.errorMessage);
			return false;
		}

	}

}
