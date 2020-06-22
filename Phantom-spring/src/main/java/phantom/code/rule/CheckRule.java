package phantom.code.rule;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import phantom.aop.validation.annotation.Check;
import phantom.code.checker.CodeCheckException;
import phantom.common.UString;
import phantom.reflect.UReflect;

/**
 * 检查@Check注解
 * @see phantom.aop.validation.annotation.Check
 * @author Frodez
 */
@Slf4j
public class CheckRule implements Rule {

	@Override
	public boolean support(Method method) throws CodeCheckException {
		return true;
	}

	@Override
	public void check(Method method) throws CodeCheckException {
		if (method.getParameterCount() == 0) {
			if (method.getDeclaringClass().isAnnotationPresent(Check.class)) {
				String warn = UString.concat(Util.classText(Check.class), "注解在类上使用时,不会作用于无参数的方法", UReflect.fullName(method));
				log.warn(warn);
			}
			throw new CodeCheckException(Util.classText(Check.class), "不能在无参数的方法", UReflect.fullName(method), "上使用");
		}
	}

}
