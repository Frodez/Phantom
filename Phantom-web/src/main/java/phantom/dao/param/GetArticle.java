package phantom.dao.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import phantom.aop.validation.annotation.Mapping;
import phantom.constant.SortType;
import phantom.mvc.data.QueryPage;

/**
 * 获取文章信息参数
 * @author Frodez
 */
@Data
@ApiModel(description = "获取文章信息参数")
public class GetArticle implements Serializable {

	private static final long serialVersionUID = 1L;

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
	 * 起始创建时间
	 */
	@ApiModelProperty("起始创建时间")
	private Date startCreateTime;

	/**
	 * 截止创建时间
	 */
	@ApiModelProperty("截止创建时间")
	private Date endCreateTime;

	/**
	 * 更新时间排序条件
	 */
	@NotNull
	@Mapping(SortType.class)
	private Byte updateTime;

	/**
	 * 起始更新时间
	 */
	@ApiModelProperty("起始更新时间")
	private Date startUpdateTime;

	/**
	 * 截止更新时间
	 */
	@ApiModelProperty("截止更新时间")
	private Date endUpdateTime;

	/**
	 * 点击量排序条件
	 */
	@NotNull
	@Mapping(SortType.class)
	private Byte clicks;

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
