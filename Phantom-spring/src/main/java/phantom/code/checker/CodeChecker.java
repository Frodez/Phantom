package phantom.code.checker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.util.Assert;
import phantom.code.rule.Rule;
import phantom.reflect.UBean;

/**
 * 代码检查器
 * @author Frodez
 */
public class CodeChecker {

	private List<Rule> rules;

	public CodeChecker(List<Rule> rules) {
		Assert.notNull(rules, "rules must not be null");
		this.rules = rules;
	}

	public boolean needCheck() {
		return !rules.isEmpty();
	}

	public void checkClass(Class<?> klass) throws CodeCheckException {
		Assert.notNull(klass, "klass must not be null");
		try {
			for (Field field : UBean.getSetterFields(klass)) {
				checkField(field);
			}
		} catch (Exception e) {
			throw new CodeCheckException(klass.getName(), e.getMessage());
		}
	}

	public void checkField(Field field) throws CodeCheckException {
		Assert.notNull(field, "field must not be null");
		for (Rule rule : rules) {
			if (rule.support(field)) {
				rule.check(field);
			}
		}
	}

	public void checkMethod(Method method) throws CodeCheckException {
		Assert.notNull(method, "field must not be null");
		for (Rule rule : rules) {
			if (rule.support(method)) {
				rule.check(method);
			}
		}
	}

}
