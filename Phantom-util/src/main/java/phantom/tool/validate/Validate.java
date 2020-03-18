package phantom.tool.validate;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import phantom.common.UString;

/**
 * 对象验证工具类
 * @author Frodez
 */
public class Validate {

	/**
	 * 验证引擎
	 */
	private static Validator engine;

	/**
	 * 快速失败配置
	 */
	private static boolean failFast;

	static {
		failFast = true;
		HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class).configure();
		configuration.failFast(failFast);
		engine = configuration.buildValidatorFactory().getValidator();
	}

	/**
	 * 更改错误信息
	 * @author Frodez
	 */
	public static void changeMessage(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

	/**
	 * 对方法参数进行验证,如果验证通过,返回null<br>
	 * <strong>用于AOP,因为AOP做出了保证,故无NPE检查。如需要单独使用,请保证参数均非null。</strong>
	 * @param instance 需要验证的方法所在类实例
	 * @param method 需要验证的方法
	 * @param args 方法参数
	 * @author Frodez
	 */
	public static String validateParam(final Object instance, final Method method, final Object[] args) {
		Set<ConstraintViolation<Object>> set = engine.forExecutables().validateParameters(instance, method, args);
		if (set.isEmpty()) {
			return null;
		}
		return failFast ? getErrorMessage(set.iterator().next()) : getErrorMessage(set);
	}

	/**
	 * 对对象进行验证,如果验证通过,返回null<br>
	 * @param Object 需要验证的对象
	 * @author Frodez
	 */
	public static String validate(final Object object) {
		return validate(object, "object is null");
	}

	/**
	 * 对对象进行验证,如果验证通过,返回null
	 * @param Object 需要验证的对象
	 * @param nullMessage 验证对象为空时返回的字符串
	 * @author Frodez
	 */
	public static String validate(final Object object, String nullMessage) {
		if (object == null) {
			return nullMessage;
		}
		Set<ConstraintViolation<Object>> set = engine.validate(object);
		if (set.isEmpty()) {
			return null;
		}
		return failFast ? getErrorMessage(set.iterator().next()) : getErrorMessage(set);
	}

	/**
	 * 获取格式化的错误信息
	 * @author Frodez
	 */
	private static String getErrorMessage(Set<ConstraintViolation<Object>> violations) {
		List<String> messages = violations.stream().map(Validate::getErrorMessage).filter((message) -> message != null).collect(Collectors
			.toList());
		return String.join(";\n", messages);
	}

	/**
	 * 获取格式化的错误信息
	 * @author Frodez
	 */
	private static String getErrorMessage(ConstraintViolation<Object> violation) {
		String errorSource = getErrorSource(violation);
		return errorSource == null ? violation.getMessage() : UString.concat(errorSource, ":", violation.getMessage());
	}

	/**
	 * 获取错误信息源
	 * @author Frodez
	 */
	private static String getErrorSource(ConstraintViolation<Object> violation) {
		Stream<Node> stream = StreamSupport.stream(violation.getPropertyPath().spliterator(), false);
		List<String> nodes = stream.filter(isErrorSouce).map(Path.Node::toString).collect(Collectors.toList());
		return nodes.isEmpty() ? null : String.join(".", nodes);
	}

	/**
	 * 判断是否为所需的错误信息节点
	 * @author Frodez
	 */
	private static Predicate<Node> isErrorSouce = (node) -> {
		switch (node.getKind()) {
			case PROPERTY :
				return true;
			case PARAMETER :
				return true;
			case CROSS_PARAMETER :
				return true;
			case CONTAINER_ELEMENT :
				return true;
			default :
				return false;
		}
	};

}
