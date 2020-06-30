package phantom.dao.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import phantom.constant.AvailableStatus;
import phantom.tool.validate.annotation.Mapping;

/**
 * @description 标签表
 * @table tb_tag
 */
@Data
@Entity
@Table(name = "tb_tag")
@ApiModel(description = "标签信息")
public class Tag implements Serializable {

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
	 * 文章ID(不能为空)
	 */
	@NotNull
	@Column(name = "article_id")
	@ApiModelProperty("文章ID")
	private Long articleId;

	/**
	 * 标签名(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "tag", length = 255)
	@ApiModelProperty("标签名")
	private String tag;

	/**
	 * 状态 1:可用 2:不可用(不能为空,默认值:1)
	 */
	@NotNull
	@Column(name = "status")
	@Mapping(AvailableStatus.class)
	private Byte status = 1;
}
