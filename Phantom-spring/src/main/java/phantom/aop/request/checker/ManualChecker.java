package phantom.aop.request.checker;

/**
 * 阻塞型重复请求检查接口
 * @author Frodez
 */
public interface ManualChecker extends Checker {

	/**
	 * 加锁
	 * @author Frodez
	 */
	void lock(String key);

	/**
	 * 解锁
	 * @author Frodez
	 */
	void free(String key);

}
