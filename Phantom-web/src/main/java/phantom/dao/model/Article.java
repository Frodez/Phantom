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
 * @description 文章表
 * @table tb_article
 */
@Data
@Entity
@Table(name = "tb_article")
@ApiModel(description = "文章信息")
public class Article implements Serializable {

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
	 * 更新时间(不能为空)
	 */
	@NotNull
	@Column(name = "update_time")
	@ApiModelProperty("更新时间")
	private Date updateTime;

	/**
	 * 作者ID(不能为空)
	 */
	@NotNull
	@Column(name = "author_id")
	@ApiModelProperty("作者ID")
	private Long authorId;

	/**
	 * 分类ID(不能为空)
	 */
	@NotNull
	@Column(name = "category_id")
	@ApiModelProperty("分类ID")
	private Long categoryId;

	/**
	 * 文章名(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "name", length = 255)
	@ApiModelProperty("文章名")
	private String name;

	/**
	 * 文章简介(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "introduction", length = 255)
	@ApiModelProperty("文章简介")
	private String introduction;

	/**
	 * 状态 1:可用 2:不可用(不能为空,默认值:1)
	 */
	@NotNull
	@Column(name = "status")
	@Mapping(AvailableStatus.class)
	private Byte status = 1;

	/**
	 * 点击量(不能为空,默认值:0)
	 */
	@NotNull
	@Column(name = "clicks")
	@ApiModelProperty("点击量")
	private Long clicks = 0L;

	/**
	 * 喜欢(不能为空,默认值:0)
	 */
	@NotNull
	@Column(name = "likes")
	@ApiModelProperty("喜欢")
	private Long likes = 0L;

	/**
	 * 不喜欢(不能为空,默认值:0)
	 */
	@NotNull
	@Column(name = "dislikes")
	@ApiModelProperty("不喜欢")
	private Long dislikes = 0L;

	/**
	 * 文章内容(不能为空)
	 */
	@NotBlank
	@Length(max = 16777215)
	@Column(name = "content", length = 16777215)
	@ApiModelProperty("文章内容")
	private String content;
}
