package phantom.constant;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import phantom.annotation.Description;
import phantom.tool.validate.annotation.Mapping.IMapping;
import phantom.tool.validate.annotation.Mapping.MappingHelper;

/**
 * 排序类型
 * @author Frodez
 */
@Getter
@AllArgsConstructor
@Description(name = "排序类型")
public enum SortType implements IMapping<Byte> {

	/**
	 * 不排序
	 */
	NONE((byte) 0, "不排序"),
	/**
	 * 升序
	 */
	ASC((byte) 1, "升序"),
	/**
	 * 降序
	 */
	DESC((byte) 2, "降序");

	private Byte val;

	private String desc;

	private static final Map<Byte, SortType> enumMap = MappingHelper.generateMap(SortType.class);

	/**
	 * 默认值
	 */
	public static final Byte defaultValue = NONE.val;

	public static SortType of(Byte value) {
		return enumMap.get(value);
	}

}
