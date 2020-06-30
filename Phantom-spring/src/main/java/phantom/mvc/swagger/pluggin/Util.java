package phantom.mvc.swagger.pluggin;

import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import phantom.common.UString;
import phantom.tool.validate.annotation.Mapping.IMapping;

/**
 * swagger插件工具类
 * @author Frodez
 */
@UtilityClass
final class Util {

	/**
	 * 返回描述信息
	 * @author Frodez
	 */
	public static String getDesc(List<IMapping<?>> values) {
		return String.join(", ",
				values.stream().map((item) -> UString.concat(item.getVal().toString(), ":", item.getDesc()))
						.collect(Collectors
								.toList()));
	}

}
