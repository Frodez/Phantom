package phantom.code.rule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import lombok.experimental.UtilityClass;
import phantom.code.checker.CodeCheckException;
import phantom.common.UString;

/**
 * 检查规则工具类
 * @author Frodez
 */
@UtilityClass
class Util {

	public static Method hasMethod(Class<?> klass, String method, Class<?>... parameterTypes) {
		try {
			return klass.getMethod(method);
		} catch (NoSuchMethodException e) {
			throw new CodeCheckException(klass.getCanonicalName(), "必须拥有", method, "方法");
		}
	}

	public static Method hasMethod(Class<?> klass, Class<?> see, String method, Class<?>... parameterTypes) {
		try {
			return klass.getMethod(method, parameterTypes);
		} catch (NoSuchMethodException e) {
			if (Annotation.class.isAssignableFrom(see)) {
				throw new CodeCheckException(klass.getCanonicalName(), "必须拥有", method, "方法", "详情参见", classText(see));
			} else if (Enum.class.isAssignableFrom(see)) {
				throw new CodeCheckException(klass.getCanonicalName(), "必须拥有", method, "方法", "详情参见", classText(see));
			} else {
				throw new CodeCheckException(klass.getCanonicalName(), "必须拥有", method, "方法", "详情参见", classText(see));
			}
		}
	}

	public static String classText(Class<?> klass) {
		if (klass == null) {
			return UString.EMPTY;
		} else if (Annotation.class.isAssignableFrom(klass)) {
			return UString.concat("@", klass.getCanonicalName(), "注解");
		} else if (Enum.class.isAssignableFrom(klass)) {
			return UString.concat(klass.getCanonicalName(), "枚举");
		} else {
			return UString.concat(klass.getCanonicalName(), "类");
		}
	}

}
