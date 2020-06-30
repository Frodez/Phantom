package phantom.dao.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import phantom.constant.UserType;
import phantom.tool.validate.annotation.Mapping;

/**
 * 用户信息
 * @author Frodez
 */
@Data
@ApiModel(description = "用户信息")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@NotNull
	@ApiModelProperty("用户ID")
	private Long id;

	/**
	 * 创建时间
	 */
	@NotNull
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 用户名
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("用户名")
	private String name;

	/**
	 * 邮箱
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("邮箱")
	private String email;

	/**
	 * 用户类型 1:管理员 2:普通用户
	 */
	@NotNull
	@Mapping(UserType.class)
	private Byte type = 2;

}
