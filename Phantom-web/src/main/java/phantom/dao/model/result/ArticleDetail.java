package phantom.dao.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(description = "文章详细信息")
public class ArticleDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 文章ID
	 */
	@NotNull
	@ApiModelProperty("文章ID")
	private Long id;

	/**
	 * 创建时间
	 */
	@NotNull
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@NotNull
	@ApiModelProperty("更新时间")
	private Date updateTime;

	/**
	 * 作者ID
	 */
	@NotNull
	@ApiModelProperty("作者ID")
	private Long authorId;

	/**
	 * 作者名
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("作者名")
	private String author;

	/**
	 * 分类ID
	 */
	@NotNull
	@ApiModelProperty("分类ID")
	private Long categoryId;

	/**
	 * 分类ID
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("分类名")
	private String category;

	/**
	 * 文章名
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("文章名")
	private String name;

	/**
	 * 文章简介
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("文章简介")
	private String introduction;

	/**
	 * 文章内容
	 */
	@NotBlank
	@Length(max = 16777215)
	@ApiModelProperty("文章内容")
	private String content;

	/**
	 * 点击量
	 */
	@NotNull
	@ApiModelProperty("点击量")
	private Long clicks;

	/**
	 * 喜欢
	 */
	@NotNull
	@ApiModelProperty("喜欢")
	private Long likes;

	/**
	 * 不喜欢
	 */
	@NotNull
	@ApiModelProperty("不喜欢")
	private Long dislikes;

	/**
	 * 标签
	 */
	@ApiModelProperty("标签")
	private List<String> tags;

}
