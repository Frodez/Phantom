package phantom.mybatis.result;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义mybatis ResultHandler注解<br>
 * @author Frodez
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomResultHandler {

	/**
	 * 自定义的ResultHandler类型,请注意返回值类型的处理
	 * @author Frodez
	 */
	Class<? extends CustomHandler> value();

}
