package phantom.dao.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import phantom.constant.SortType;
import phantom.mvc.data.QueryPage;
import phantom.tool.validate.annotation.Mapping;

/**
 * 获取评论信息参数
 * @author Frodez
 */
@Data
@ApiModel(description = "获取评论信息参数")
public class GetComment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 文章ID
	 */
	@NotNull
	@ApiModelProperty("文章ID")
	private Long articleId;

	/**
	 * 分页信息
	 */
	@NotNull
	private QueryPage page;

	/**
	 * 创建时间排序条件
	 */
	@NotNull
	@Mapping(SortType.class)
	private Byte createTime;

	/**
	 * 喜欢数排序条件
	 */
	@NotNull
	@Mapping(SortType.class)
	private Byte likes;

	/**
	 * 不喜欢数排序条件
	 */
	@NotNull
	@Mapping(SortType.class)
	private Byte dislikes;

}
