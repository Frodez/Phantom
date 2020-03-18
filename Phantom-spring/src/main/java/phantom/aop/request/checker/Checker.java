package phantom.aop.request.checker;

/**
 * 检查接口
 * @author Frodez
 */
public interface Checker {

	/**
	 * 根据key判断是否满足条件,满足返回true,否则返回false
	 * @author Frodez
	 */
	boolean check(String key);

}
