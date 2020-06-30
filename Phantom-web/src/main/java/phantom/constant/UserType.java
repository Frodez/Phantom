package phantom.constant;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import phantom.annotation.Description;
import phantom.tool.validate.annotation.Mapping.IMapping;
import phantom.tool.validate.annotation.Mapping.MappingHelper;

/**
 * 用户类型
 * @author Frodez
 */
@Getter
@AllArgsConstructor
@Description(name = "用户类型")
public enum UserType implements IMapping<Byte> {

	/**
	 * 管理员
	 */
	ADMIN((byte) 1, "管理员"),
	/**
	 * 普通用户
	 */
	NORMAL((byte) 2, "普通用户");

	private Byte val;

	private String desc;

	private static final Map<Byte, UserType> enumMap = MappingHelper.generateMap(UserType.class);

	/**
	 * 默认值
	 */
	public static final Byte defaultValue = NORMAL.val;

	public static UserType of(Byte value) {
		return enumMap.get(value);
	}

}
