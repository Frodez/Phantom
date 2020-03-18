package phantom.aop.request.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import javax.validation.constraints.Positive;
import lombok.experimental.UtilityClass;
import org.springframework.core.annotation.AnnotationUtils;
import phantom.code.checker.CodeCheckException;
import phantom.util.reflect.UReflect;

/**
 * 请求限流注解
 * @author Frodez
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

	/**
	 * 每秒每token限制请求数,默认值100.0
	 * @author Frodez
	 */
	@Positive
	double value() default 100.0;

	/**
	 * 超时时间,默认值3000毫秒
	 * @author Frodez
	 */
	@Positive
	long timeout() default 3000;

	@UtilityClass
	public static class LimitHelper {

		public static Limit get(Method method, Class<?> targetClass) {
			Limit annotation = AnnotationUtils.findAnnotation(method, Limit.class);
			if (annotation == null) {
				annotation = AnnotationUtils.findAnnotation(targetClass, Limit.class);
			}
			return annotation;
		}

		public static void check(Method method, Limit annotation) {
			if (annotation.value() <= 0) {
				throw new CodeCheckException("方法", UReflect.fullName(method), "的每秒每token限制请求数必须大于0!");
			}
			if (annotation.timeout() <= 0) {
				throw new CodeCheckException("方法", UReflect.fullName(method), "的超时时间必须大于0!");
			}
		}
	}

}
