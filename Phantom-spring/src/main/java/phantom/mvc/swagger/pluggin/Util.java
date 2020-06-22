package phantom.mvc.swagger.pluggin;

import static springfox.documentation.spi.schema.contexts.ModelContext.returnValue;

import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import phantom.aop.validation.annotation.Mapping.IMapping;
import phantom.common.UString;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.contexts.OperationContext;

/**
 * swagger插件工具类
 * @author Frodez
 */
@UtilityClass
final class Util {

	/**
	 * 从OperationContext转换出ModelContext
	 * @author Frodez
	 */
	public static ModelContext resolveModelContext(OperationContext context, Type type) {
		String group = context.getGroupName();
		DocumentationType documentation = context.getDocumentationType();
		AlternateTypeProvider provider = context.getAlternateTypeProvider();
		GenericTypeNamingStrategy strategy = context.getGenericsNamingStrategy();
		@SuppressWarnings("rawtypes") ImmutableSet<Class> types = context.getIgnorableParameterTypes();
		return returnValue(group, type, documentation, provider, strategy, types);
	}

	/**
	 * 返回描述信息
	 * @author Frodez
	 */
	public static String getDesc(List<IMapping<?>> values) {
		return String.join(", ", values.stream().map((item) -> UString.concat(item.getVal().toString(), ":", item.getDesc())).collect(Collectors
			.toList()));
	}

}
