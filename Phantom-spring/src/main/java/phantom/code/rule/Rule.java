package phantom.code.rule;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import phantom.code.checker.CodeCheckException;

/**
 * 代码检查接口
 * @author Frodez
 */
public interface Rule {

	/**
	 * 是否支持类型检查
	 * @author Frodez
	 */
	default boolean support(Class<?> klass) throws CodeCheckException {
		return false;
	};

	/**
	 * 检查类型
	 * @author Frodez
	 */
	default void check(Class<?> klass) throws CodeCheckException {
	};

	/**
	 * 是否支持字段检查
	 * @author Frodez
	 */
	default boolean support(Field field) throws CodeCheckException {
		return false;
	};

	/**
	 * 检查字段
	 * @author Frodez
	 */
	default void check(Field field) throws CodeCheckException {
	};

	/**
	 * 是否支持方法检查
	 * @author Frodez
	 */
	default boolean support(Method method) throws CodeCheckException {
		return false;
	};

	/**
	 * 检查方法
	 * @author Frodez
	 */
	default void check(Method method) throws CodeCheckException {
	};

}
