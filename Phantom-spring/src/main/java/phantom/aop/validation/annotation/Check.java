package phantom.aop.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证方法参数<br>
 * 作用于类上时,类中所有方法都会进行验证。<br>
 * 但是请注意,如果有的方法不需要验证,则必须为需要验证的方法单独设置本注解,而不是在类上设置本注解。
 * @author Frodez
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {

}
