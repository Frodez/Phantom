package phantom.code.rule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.Constraint;
import phantom.aop.validation.annotation.Mapping;
import phantom.code.checker.CodeCheckException;
import phantom.reflect.UReflect;

/**
 * 检查验证注解是否符合类型
 * @author Frodez
 */
public class GenericConstraintRule implements Rule {

	private Set<Class<?>> excepts = Set.of(Mapping.class);

	@Override
	public boolean support(Field field) throws CodeCheckException {
		return true;
	}

	@Override
	public void check(Field field) throws CodeCheckException {
		Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			if (excepts.contains(annotation.annotationType())) {
				continue;
			}
			Constraint constraint = annotation.annotationType().getAnnotation(Constraint.class);
			if (constraint != null) {
				for (Class<?> klass : getAllSuitableClasses(field.getType(), constraint)) {
					if (!isSuitable(field.getType(), klass)) {
						throw new CodeCheckException(UReflect.fullName(field), "的类型必须是", klass.getCanonicalName(), "或者其子类");
					}
				}
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
			Annotation[] annotations = parameter.getAnnotations();
			for (Annotation annotation : annotations) {
				if (excepts.contains(annotation.annotationType())) {
					continue;
				}
				Constraint constraint = annotation.annotationType().getAnnotation(Constraint.class);
				if (constraint != null) {
					for (Class<?> klass : getAllSuitableClasses(parameter.getType(), constraint)) {
						if (!isSuitable(parameter.getType(), klass)) {
							throw new CodeCheckException("方法", UReflect.fullName(method), "的参数", parameter.getName(), "的类型必须是", klass
								.getCanonicalName(), "或者其子类");
						}
					}
				}
			}
		}
	}

	private List<Class<?>> getAllSuitableClasses(Class<?> klass, Constraint constraint) {
		List<Class<?>> classes = new ArrayList<>();
		for (var validator : constraint.validatedBy()) {
			Class<?> actualClass = (Class<?>) ((ParameterizedType) validator.getGenericInterfaces()[0]).getActualTypeArguments()[1];
			classes.add(actualClass);
		}
		return classes;
	}

	private boolean isSuitable(Class<?> actualClass, Class<?> klass) {
		return klass == Object.class ? true : actualClass.isAssignableFrom(klass);
	}

}
