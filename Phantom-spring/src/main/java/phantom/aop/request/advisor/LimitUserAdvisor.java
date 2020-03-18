package phantom.aop.request.advisor;

import com.google.common.util.concurrent.RateLimiter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.stereotype.Component;
import phantom.aop.UAOP;
import phantom.aop.request.annotation.Limit;
import phantom.aop.request.annotation.Limit.LimitHelper;
import phantom.common.Pair;
import phantom.configuration.CacheProperties;
import phantom.mvc.data.Result;

/**
 * 请求限流AOP切面
 * @author Frodez
 */
@Component
public class LimitUserAdvisor implements PointcutAdvisor {

	/**
	 * 限流器
	 */
	private Map<Method, Pair<RateLimiter, Long>> limitCache = new HashMap<>();

	/**
	 * 执行器
	 */
	private Advice advice = (MethodInterceptor) invocation -> {
		Pair<RateLimiter, Long> pair = limitCache.get(invocation.getMethod());
		if (!pair.getFirst().tryAcquire(pair.getSecond(), CacheProperties.TIME_UNIT)) {
			return Result.busy();
		}
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
			Limit annotation = LimitHelper.get(method, targetClass);
			if (annotation == null) {
				return false;
			}
			LimitHelper.check(method, annotation);
			if (!UAOP.isResultAsReturn(method)) {
				return false;
			}
			Pair<RateLimiter, Long> pair = new Pair<>(RateLimiter.create(annotation.value()), annotation.timeout());
			limitCache.put(method, pair);
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
