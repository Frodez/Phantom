package phantom.code.rule;

import java.lang.reflect.Method;
import phantom.aop.validation.annotation.Check;
import phantom.code.checker.CodeCheckException;
import phantom.reflect.UReflect;

/**
 * 检查@Check注解
 * @see phantom.aop.validation.annotation.Check
 * @author Frodez
 */
public class CheckRule implements Rule {

	@Override
	public boolean support(Method method) throws CodeCheckException {
		return true;
	}

	@Override
	public void check(Method method) throws CodeCheckException {
		if (method.getParameterCount() == 0) {
			throw new CodeCheckException(Util.classText(Check.class), "不能在无参数的方法", UReflect.fullName(method), "上使用");
		}
	}

}
