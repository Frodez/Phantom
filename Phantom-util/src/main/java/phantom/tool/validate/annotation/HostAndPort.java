package phantom.tool.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * 检查字符串是否可被解析为Host:Port
 * @author Frodez
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HostAndPort.Validator.class)
public @interface HostAndPort {

	String message() default "${validatedValue}必须可被解析为Host:Port";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 格式验证器
	 * @author Frodez
	 */
	class Validator implements ConstraintValidator<HostAndPort, String> {

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			try {
				com.google.common.net.HostAndPort.fromString(value);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

}
