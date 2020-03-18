package phantom.aop.request.advisor;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import phantom.aop.UAOP;
import phantom.aop.request.annotation.RepeatLock;
import phantom.aop.request.annotation.TimeoutLock;
import phantom.aop.request.annotation.RepeatLock.RepeatLockHelper;
import phantom.aop.request.checker.ManualChecker;
import phantom.mvc.UServlet;
import phantom.mvc.data.Result;
import phantom.reflect.UReflect;

/**
 * 控制重复请求AOP切面
 * @author Frodez
 */
@Slf4j
@Component
public class RepeatAdvisor implements PointcutAdvisor {

	/**
	 * 阻塞型重复请求检查器
	 */
	@Autowired
	private ManualChecker checker;

	/**
	 * 执行器
	 */
	private Advice advice = (MethodInterceptor) invocation -> {
		HttpServletRequest request = UServlet.request();
		String key = Util.servletKey(UReflect.fullName(invocation.getMethod()), request);
		try {
			if (checker.check(key)) {
				log.info("重复请求:IP地址{}", UServlet.getAddr(request));
				return Result.repeatRequest();
			}
			checker.lock(key);
			return invocation.proceed();
		} finally {
			checker.free(key);
		}
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
			RepeatLock annotation = RepeatLockHelper.get(method, targetClass);
			if (annotation == null) {
				return false;
			}
			//如果在类上发现了TimeoutLock,则让给TimeoutLock
			if (AnnotationUtils.findAnnotation(method, TimeoutLock.class) != null) {
				return false;
			}
			if (!UAOP.isResultAsReturn(method)) {
				return false;
			}
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
