package phantom.dao.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 评论信息
 * @author Frodez
 */
@Data
@ApiModel(description = "评论信息")
public class CommentInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 评论ID
	 */
	@NotNull
	@ApiModelProperty("评论ID")
	private Long id;

	/**
	 * 用户ID(0为匿名)
	 */
	@NotNull
	@ApiModelProperty("用户ID(0为匿名)")
	private Long userId;

	/**
	 * 用户名(0为匿名)
	 */
	@ApiModelProperty("用户名")
	private String username;

	/**
	 * 文章ID
	 */
	@NotNull
	@ApiModelProperty("文章ID")
	private Long articleId;

	/**
	 * 创建时间
	 */
	@NotNull
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 喜欢
	 */
	@NotNull
	@ApiModelProperty("喜欢")
	private Long likes = 0L;

	/**
	 * 不喜欢
	 */
	@NotNull
	@ApiModelProperty("不喜欢")
	private Long dislikes = 0L;

	/**
	 * 评论内容
	 */
	@NotBlank
	@Length(max = 65535)
	@ApiModelProperty("评论内容")
	private String content;

}
