package phantom.aop.request.checker;

/**
 * 自动超时型重复请求检查接口
 * @author Frodez
 */
public interface AutoChecker extends Checker {

	/**
	 * 加锁
	 * @param key 对应key
	 * @param timeout 超时时间
	 * @author Frodez
	 */
	void lock(String key, long timeout);

}
