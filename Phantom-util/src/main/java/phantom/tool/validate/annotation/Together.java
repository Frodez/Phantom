package phantom.tool.validate.annotation;

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
import phantom.reflect.UReflect;
import phantom.tool.validate.Validate;

/**
 * 判断该类实体的指定字段是否至少同时为null或者同时不为null<br>
 * 可以在实体上添加多个本注解<br>
 * 使用方式:<br>
 * 1.
 *
 * <pre>
 * &#64;Together({ "a", "b" })
 * &#64;Together({ "c", "d", "e" })
 * &#64;Together({ "d", "e" })
 * </pre>
 *
 * 2.
 *
 * <pre>
 * &#64;Together({ &#64;Together({ "a", "b" }), &#64;Together({ "c", "d", "e" }), &#64;Together({ "d", "e" }) })
 * </pre>
 *
 * @see phantom.tool.validate.annotation.Togethers
 * @author Frodez
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Togethers.class)
@Constraint(validatedBy = Together.Validator.class)
public @interface Together {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 指定的字段名,不设置的话则默认为所有字段
	 * @author Frodez
	 */
	String[] value() default {};

	/**
	 * TogetherHelper工具类,仅供Together相关功能使用!!!
	 * @author Frodez
	 */
	@UtilityClass
	public static class TogetherHelper {

		private static final Map<Together, TogetherInfo> CACHE = new ConcurrentHashMap<>();

		private TogetherInfo get(Class<?> klass, Together annotation) {
			return CACHE.computeIfAbsent(annotation, (key) -> generate(klass, key));
		}

		@SneakyThrows
		private static TogetherInfo generate(Class<?> klass, Together annotation) {
			TogetherInfo result = new TogetherInfo();
			String[] fieldNames = annotation.value();
			if (UEmpty.yes(fieldNames)) {
				result.fields = klass.getDeclaredFields();
				result.errorMessage = UString.concat(klass.getSimpleName(), "要么全为null,要么全不为null");
			} else {
				Field[] fields = new Field[fieldNames.length];
				for (int i = 0; i < fieldNames.length; i++) {
					String fieldName = fieldNames[i];
					fields[i] = klass.getDeclaredField(fieldName);
				}
				result.fields = fields;
				result.errorMessage = UString.concat(klass.getSimpleName(), "的", String.join(", ", fieldNames),
						"字段要么全为null,要么全不为null");
			}
			return result;
		}

		private static class TogetherInfo {

			public Field[] fields;

			public String errorMessage;
		}

	}

	class Validator implements ConstraintValidator<Together, Object> {

		private Together annotation;

		@Override
		public void initialize(Together annotation) {
			this.annotation = annotation;
		}

		@Override
		@SneakyThrows
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			if (value == null) {
				return true;
			}
			TogetherHelper.TogetherInfo info = TogetherHelper.get(value.getClass(), annotation);
			int nullCount = 0;
			for (int i = 0; i < info.fields.length;) {
				Field field = info.fields[i++];// 警告:这里同时也做了i=i+1
				if (UReflect.get(field, value) == null) {
					nullCount++;
				}
				if (nullCount != 0 && nullCount != i) {
					Validate.changeMessage(context, info.errorMessage);
					return false;
				}
			}
			return true;
		}

	}

}
