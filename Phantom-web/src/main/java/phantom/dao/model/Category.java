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

/**
 * @description 分类表
 * @table tb_category
 */
@Data
@Entity
@Table(name = "tb_category")
@ApiModel(description = "分类信息")
public class Category implements Serializable {

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
	 * 类别名(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "name", length = 255)
	@ApiModelProperty("类别名")
	private String name;

	/**
	 * 状态 1:可用 2:不可用(不能为空,默认值:1)
	 */
	@NotNull
	@Column(name = "status")
	@Mapping(AvailableStatus.class)
	private Byte status = 1;
}
