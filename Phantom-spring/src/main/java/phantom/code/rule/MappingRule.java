package phantom.code.rule;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

import phantom.code.checker.CodeCheckException;
import phantom.tool.validate.annotation.Mapping;
import phantom.tool.validate.annotation.Mapping.MappingHelper;

/**
 * 检查@Mapping注解
 * @see phantom.tool.validate.annotation.Mapping
 * @author Frodez
 */
public class MappingRule implements Rule {

	private Set<Class<?>> legalEnumCheckCache = new HashSet<>();

	@Override
	public boolean support(Field field) throws CodeCheckException {
		return true;
	}

	@Override
	public void check(Field field) throws CodeCheckException {
		Mapping annotation = field.getAnnotation(Mapping.class);
		if (annotation != null) {
			MappingHelper.isLegal(field.getType(), annotation.value());
			synchronized (legalEnumCheckCache) {
				legalEnumCheckCache.add(annotation.value());
			}
		}
	}

	@Override
	public boolean support(Method method) throws CodeCheckException {
		return true;
	}

	@Override
	public void check(Method method) throws CodeCheckException {
		for (Parameter parameter : method.getParameters()) {
			Mapping annotation = parameter.getAnnotation(Mapping.class);
			if (annotation != null) {
				MappingHelper.isLegal(parameter.getType(), annotation.value());
				synchronized (legalEnumCheckCache) {
					legalEnumCheckCache.add(annotation.value());
				}
			}
		}
	}

}
