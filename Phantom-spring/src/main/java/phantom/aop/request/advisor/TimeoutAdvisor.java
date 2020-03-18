package phantom.aop.request.advisor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import phantom.aop.UAOP;
import phantom.aop.request.annotation.TimeoutLock;
import phantom.aop.request.annotation.TimeoutLock.TimeoutLockHelper;
import phantom.aop.request.checker.AutoChecker;
import phantom.mvc.UServlet;
import phantom.mvc.data.Result;
import phantom.reflect.UReflect;

/**
 * 控制重复请求AOP切面
 * @author Frodez
 */
@Slf4j
@Component
public class TimeoutAdvisor implements PointcutAdvisor {

	/**
	 * 自动超时型重复请求检查器
	 */
	@Autowired
	private AutoChecker checker;

	/**
	 * 注解配置缓存
	 */
	private Map<String, Long> timeoutCache = new HashMap<>();

	/**
	 * 执行器
	 */
	private Advice advice = (MethodInterceptor) invocation -> {
		HttpServletRequest request = UServlet.request();
		String name = UReflect.fullName(invocation.getMethod());
		String key = Util.servletKey(name, request);
		if (checker.check(key)) {
			log.info("重复请求:IP地址{}", UServlet.getAddr(request));
			return Result.repeatRequest();
		}
		checker.lock(key, timeoutCache.get(name));
		return invocation.proceed();
	};

	/**
	 * 匹配器
	 */
	private MethodMatcher methodMatcher = new MethodMatcher() {

		@Override
		public boolean matches(Method method, Class<?> targetClass, Object... args) {
			//isRuntime()方法返回值为false时,不会进行运行时判断
			return false;
		}

		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			if (!UAOP.isController(targetClass)) {
				return false;
			}
			//这里可以进行运行前检查
			TimeoutLock annotation = TimeoutLockHelper.get(method, targetClass);
			if (annotation == null) {
				return false;
			}
			TimeoutLockHelper.check(method, annotation);
			if (!UAOP.isResultAsReturn(method)) {
				return false;
			}
			timeoutCache.put(UReflect.fullName(method), annotation.value());
			return true;
		}

		@Override
		public boolean isRuntime() {
			return false;
		}
	};

	private Pointcut pointcut = new Pointcut() {

		@Override
		public MethodMatcher getMethodMatcher() {
			return methodMatcher;
		}

		@Override
		public ClassFilter getClassFilter() {
			return clazz -> true;
		}

	};

	@Override
	public Advice getAdvice() {
		return advice;
	}

	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}

	@Override
	public boolean isPerInstance() {
		return true;
	}

}
