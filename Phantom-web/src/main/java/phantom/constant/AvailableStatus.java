package phantom.constant;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import phantom.annotation.Description;
import phantom.tool.validate.annotation.Mapping.IMapping;
import phantom.tool.validate.annotation.Mapping.MappingHelper;

/**
 * 可用状态
 * @author Frodez
 */
@Getter
@AllArgsConstructor
@Description(name = "可用状态")
public enum AvailableStatus implements IMapping<Byte> {

	/**
	 * 可用
	 */
	YES((byte) 1, "可用"),
	/**
	 * 不可用
	 */
	NO((byte) 2, "不可用");

	private Byte val;

	private String desc;

	private static final Map<Byte, AvailableStatus> enumMap = MappingHelper.generateMap(AvailableStatus.class);

	/**
	 * 默认值
	 */
	public static final Byte defaultValue = YES.val;

	public static AvailableStatus of(Byte value) {
		return enumMap.get(value);
	}

}
