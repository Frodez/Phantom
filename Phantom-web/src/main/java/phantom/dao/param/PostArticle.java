package phantom.dao.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 发布文章信息参数
 * @author Frodez
 */
@Data
@ApiModel(description = "发布文章信息参数")
public class PostArticle implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 分类ID(不能为空)
	 */
	@NotNull
	@ApiModelProperty("分类ID")
	private Long categoryId;

	/**
	 * 文章名(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("文章名")
	private String name;

	/**
	 * 文章简介(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("文章简介")
	private String introduction;

	/**
	 * 文章内容(不能为空)
	 */
	@NotBlank
	@Length(max = 16777215)
	@ApiModelProperty("文章内容")
	private String content;

	/**
	 * 标签
	 */
	@ApiModelProperty("标签")
	private List<String> tags;

}
