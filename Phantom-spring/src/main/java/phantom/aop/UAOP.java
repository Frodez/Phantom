package phantom.aop;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.experimental.UtilityClass;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RestController;

import phantom.code.checker.CodeCheckException;
import phantom.common.UEmpty;
import phantom.mvc.data.Result.Value;
import phantom.reflect.UReflect;
import phantom.tool.validate.annotation.Check;

/**
 * AOP工具类
 * @author Frodez
 */
@UtilityClass
public class UAOP {

	/**
	 * 判断该类是否为Controller
	 * @author Frodez
	 */
	public static boolean isController(Class<?> klass) {
		if (AnnotationUtils.findAnnotation(klass, Controller.class) != null) {
			return true;
		}
		if (AnnotationUtils.findAnnotation(klass, RestController.class) != null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断方法返回类型是否为AsyncResult-即ListenableFuture(类型参数T为Result)<br>
	 * 如果是AsyncResult,返回true.如果是Result,返回false.<br>
	 * 如果既不是AsyncResult,也不是Result,则抛出异常.<br>
	 * @see org.springframework.util.concurrent.ListenableFuture
	 * @see phantom.phantom.mvc.data.Result
	 * @author Frodez
	 */
	public static boolean isAsyncResultAsReturn(Method method) throws CodeCheckException {
		Type type = method.getAnnotatedReturnType().getType();
		if (isAsyncResult(type)) {
			return true;
		}
		if (isResult(type)) {
			return false;
		}
		if (!method.isAnnotationPresent(Check.class)) {
			return false;
		}
		throw new CodeCheckException("含有", "@", Check.class.getCanonicalName(), "注解的方法", UReflect.fullName(method),
				"的返回值类型必须为",
				ListenableFuture.class.getCanonicalName(), "或者", Value.class.getCanonicalName());
	}

	/**
	 * 判断方法返回类型是否为Result<br>
	 * 如果是Result,返回true.如果是AsyncResult,返回false.<br>
	 * 如果既不是AsyncResult-即ListenableFuture(类型参数T为Result),也不是Result,则抛出异常.<br>
	 * @see org.springframework.util.concurrent.ListenableFuture
	 * @see phantom.phantom.mvc.data.Result
	 * @author Frodez
	 */
	public static boolean isResultAsReturn(Method method) throws CodeCheckException {
		Type type = method.getAnnotatedReturnType().getType();
		if (isResult(type)) {
			return true;
		}
		if (isAsyncResult(type)) {
			return false;
		}
		if (!method.isAnnotationPresent(Check.class)) {
			return false;
		}
		throw new CodeCheckException("含有", "@", Check.class.getCanonicalName(), "注解的方法", UReflect.fullName(method),
				"的返回值类型必须为",
				ListenableFuture.class.getCanonicalName(), "或者", Value.class.getCanonicalName());
	}

	private static boolean isResult(Type type) {
		if (type instanceof Class) {
			return Value.class.isAssignableFrom((Class<?>) type);
		}
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return Value.class.isAssignableFrom((Class<?>) parameterizedType.getRawType());
		}
		return false;
	}

	private static boolean isAsyncResult(Type type) {
		if (!(type instanceof ParameterizedType)) {
			return false;
		}
		Type rawType = ((ParameterizedType) type).getRawType();
		if (!(rawType instanceof Class) || !ListenableFuture.class.isAssignableFrom((Class<?>) rawType)) {
			return false;
		}
		Type[] types = ((ParameterizedType) type).getActualTypeArguments();
		if (UEmpty.yes(types)) {
			return false;
		}
		Type typeArgument = types[0];
		if (typeArgument instanceof Class) {
			return Value.class.isAssignableFrom((Class<?>) typeArgument);
		}
		if (typeArgument instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) typeArgument;
			return Value.class.isAssignableFrom((Class<?>) parameterizedType.getRawType());
		}
		return false;
	}

}
