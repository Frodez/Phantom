package phantom.tool.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Together约束注解重复功能辅助支持<br>
 * @see phantom.tool.validate.annotation.Together
 * @author Frodez
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Togethers {

	Together[] value();

}
