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

import phantom.common.UDate;

/**
 * 日期——时间格式验证注解<br>
 * <strong>如果被验证的日期字符串在业务逻辑中需要被转换为date对象,请酌情考虑是否使用此注解.<br>
 * 因为本注解的原理也是尝试将字符串进行转换,转换失败则提示错误,如果使用此注解,意味着多了一次无谓的转换.<br>
 * </strong> 以下为注解参数说明:<br>
 * <strong> message: String类型,代表验证失败时的返回信息,默认值为"参数非法!"<br>
 * nullable: boolean类型,代表对空值的处理方式,默认值为false.为true时空值可以通过验证,为false时空值不可以通过验证.<br>
 * </strong>
 * @author Frodez
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTime.Validator.class)
public @interface DateTime {

	/**
	 * 错误信息
	 * @author Frodez
	 */
	String message() default "${validatedValue}需要是yyyy-MM-dd HH:mm:ss类型的时间";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 日期符合格式验证器
	 * @author Frodez
	 */
	class Validator implements ConstraintValidator<DateTime, String> {

		/**
		 * 验证
		 * @author Frodez
		 */
		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			if (value == null) {
				return true;
			}
			return UDate.isDateTime(value);
		}

	}

}
