package phantom.aop.validation;

import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import phantom.aop.UAOP;
import phantom.code.checker.CodeChecker;
import phantom.mvc.data.Result;
import phantom.tool.validate.Validate;
import phantom.tool.validate.annotation.Check;

/**
 * 验证参数AOP<br>
 * 使用方法:在方法的实现上加入@check注解,然后在方法实现的请求参数上使用hibernate validation api支持的方式配置验证.<br>
 * @author Frodez
 */
@Component
@Order(Integer.MAX_VALUE)
public class AsyncValidationAdvisor implements PointcutAdvisor {

	@Autowired
	private CodeChecker codeChecker;

	/**
	 * 执行器
	 */
	private Advice advice = (MethodInterceptor) invocation -> {
		String msg = Validate.validateParam(invocation.getThis(), invocation.getMethod(), invocation.getArguments());
		return msg == null ? invocation.proceed() : Result.errorRequest(msg).async();
	};

	/**
	 * 匹配器
	 */
	private MethodMatcher methodMatcher = new MethodMatcher() {

		@Override
		public boolean matches(Method method, Class<?> targetClass, Object... args) {
			// isRuntime()方法返回值为false时,不会进行运行时判断
			return false;
		}

		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			// 这里可以进行运行前检查
			if (!UAOP.isAsyncResultAsReturn(method)) {
				return false;
			}
			if (AnnotationUtils.findAnnotation(method, Check.class) == null
					&& AnnotationUtils.findAnnotation(targetClass, Check.class) == null) {
				return false;
			}
			codeChecker.checkMethod(method);
			if (targetClass.isAnnotationPresent(Check.class) && method.getParameterCount() == 0) {
				return false;// 类上使用Check注解时,不会对无参数方法生效(在checkMethod后使用本判断,以提供必要的警示)
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
