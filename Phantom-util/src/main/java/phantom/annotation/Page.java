package phantom.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指本接口是用于分页查询列表的接口，请不要随意使用。
 * @author Frodez
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.SOURCE)
public @interface Page {

}
