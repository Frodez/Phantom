package phantom.code.rule;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;
import phantom.aop.validation.annotation.Check;
import phantom.code.annotation.Checkable;
import phantom.code.checker.CodeCheckException;
import phantom.reflect.UReflect;

/**
 * 检查@Valid注解
 * @see javax.validation.Valid
 * @author Frodez
 */
public class ValidRule implements Rule {

	@Override
	public boolean support(Field field) throws CodeCheckException {
		return true;
	}

	@Override
	public void check(Field field) throws CodeCheckException {
		Class<?> type = field.getType();
		if (!Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type)) {
			assertFieldValid(field);
		} else {
			checkFieldCollectionOrMap(field, (ParameterizedType) field.getGenericType());
		}
	}

	@Override
	public boolean support(Method method) throws CodeCheckException {
		return true;
	}

	@Override
	public void check(Method method) throws CodeCheckException {
		for (Parameter parameter : method.getParameters()) {
			Class<?> type = parameter.getType();
			if (!Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type)) {
				assertParameterValid(method, parameter);
			} else {
				checkParameterCollectionOrMap(method, parameter, (ParameterizedType) parameter.getParameterizedType());
			}
		}
	}

	private void assertFieldValid(Field field) {
		if (field.getAnnotation(Checkable.class) != null && field.getAnnotation(Valid.class) == null) {
			throw new CodeCheckException(UReflect.fullName(field), "需要加上", Util.classText(Valid.class));
		}
	}

	private void checkFieldCollectionOrMap(Field field, ParameterizedType genericType) {
		if (Map.class.isAssignableFrom((Class<?>) genericType.getRawType())) {
			return;
		}
		for (Type actualType : genericType.getActualTypeArguments()) {
			//如果是直接类型,直接判断
			if (actualType instanceof Class) {
				assertFieldValid(field);
				continue;
			}
			//如果是泛型类型
			if (actualType instanceof ParameterizedType) {
				checkFieldCollectionOrMap(field, (ParameterizedType) actualType);
				continue;
			}
		}
	}

	private void assertParameterValid(Method method, Parameter parameter) {
		if (parameter.getAnnotation(Checkable.class) != null && parameter.getAnnotation(Valid.class) == null) {
			throw new CodeCheckException("含有", Util.classText(Check.class), "的方法", UReflect.fullName(method), "的参数", parameter.getName(), "必须使用",
				Util.classText(Valid.class));
		}
	}

	private void checkParameterCollectionOrMap(Method method, Parameter parameter, ParameterizedType genericType) {
		if (Map.class.isAssignableFrom((Class<?>) genericType.getRawType())) {
			return;
		}
		for (Type actualType : genericType.getActualTypeArguments()) {
			//如果是直接类型,直接判断
			if (actualType instanceof Class) {
				assertParameterValid(method, parameter);
				continue;
			}
			//如果是泛型类型
			if (actualType instanceof ParameterizedType) {
				checkParameterCollectionOrMap(method, parameter, (ParameterizedType) actualType);
				continue;
			}
		}
	}

}
