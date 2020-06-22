package phantom.dao.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import phantom.aop.validation.annotation.Mapping;
import phantom.constant.AvailableStatus;
import phantom.constant.UserType;

/**
 * @description 用户表
 * @table tb_user
 */
@Data
@Entity
@Table(name = "tb_user")
@ApiModel(description = "用户信息")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID(不能为空)
	 */
	@Id
	@NotNull
	@Column(name = "id")
	@ApiModelProperty("ID")
	private Long id;

	/**
	 * 创建时间(不能为空)
	 */
	@NotNull
	@Column(name = "create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 用户名(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "name", length = 255)
	@ApiModelProperty("用户名")
	private String name;

	/**
	 * 邮箱(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "email", length = 255)
	@ApiModelProperty("邮箱")
	private String email;

	/**
	 * 密码(不能为空)
	 */
	@NotBlank
	@Length(max = 1000)
	@Column(name = "password", length = 1000)
	@ApiModelProperty("密码")
	private String password;

	/**
	 * 用户类型 1:管理员 2:普通用户(不能为空,默认值:2)
	 */
	@NotNull
	@Column(name = "type")
	@Mapping(UserType.class)
	private Byte type = 2;

	/**
	 * 状态 1:可用 2:不可用(不能为空,默认值:1)
	 */
	@NotNull
	@Column(name = "status")
	@Mapping(AvailableStatus.class)
	private Byte status = 1;
}
