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

import phantom.common.URegex;

/**
 * 字符串验证注解
 * @author Frodez
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Match.Validator.class)
public @interface Match {

	/**
	 * 错误信息
	 * @author Frodez
	 */
	String message() default "${validatedValue}需要匹配正则表达式\"{regex}\"";

	/**
	 * 使用的正则表达式
	 * @author Frodez
	 */
	String value();

	/**
	 * 正则表达式的模式
	 * @see java.util.regex.Pattern
	 * @author Frodez
	 */
	int[] flags() default {};

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 格式验证器
	 * @author Frodez
	 */
	class Validator implements ConstraintValidator<Match, String> {

		/**
		 * 验证用的格式
		 */
		private String regex;

		/**
		 * 正则表达式的模式
		 */
		private int flag;

		/**
		 * 根据注解信息初始化验证器
		 * @author Frodez
		 */
		@Override
		public void initialize(Match enumValue) {
			regex = enumValue.value();
			flag = URegex.transfer(enumValue.flags());
		}

		/**
		 * 验证
		 * @author Frodez
		 */
		@Override
		public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
			if (value == null) {
				return true;
			}
			return URegex.match(regex, value, flag);
		}

	}

}
