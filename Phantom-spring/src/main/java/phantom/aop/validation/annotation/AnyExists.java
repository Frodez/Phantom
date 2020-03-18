package phantom.aop.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AnyExist约束注解重复功能辅助支持<br>
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
 * &#64;AnyExists({ @AnyExist({ "a", "b" }), @AnyExist({ "c", "d", "e" }), @AnyExist({ "d", "e" }) })
 * </pre>
 *
 * @see phantom.aop.validation.annotation.AnyExist
 * @author Frodez
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyExists {

	AnyExist[] value();

}
