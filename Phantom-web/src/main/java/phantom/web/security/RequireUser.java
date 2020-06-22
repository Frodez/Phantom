package phantom.web.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import phantom.constant.UserType;

/**
 * 用户权限验证<br>
 * 1.优先判断方法,然后判断类
 * @author Frodez
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface RequireUser {

	/**
	 * 符合权限的用户
	 * @author Frodez
	 */
	UserType[] value();

}
