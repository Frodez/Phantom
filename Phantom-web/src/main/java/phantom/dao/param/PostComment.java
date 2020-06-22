package phantom.dao.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 发布评论信息参数
 * @author Frodez
 */
@Data
@ApiModel(description = "发布评论信息参数")
public class PostComment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否匿名
	 */
	@NotNull
	@ApiModelProperty("是否匿名")
	private Boolean anonymous;

	/**
	 * 文章ID
	 */
	@NotNull
	@ApiModelProperty("文章ID")
	private Long articleId;

	/**
	 * 评论内容
	 */
	@NotBlank
	@Length(max = 65535)
	@ApiModelProperty("评论内容")
	private String content;

}
