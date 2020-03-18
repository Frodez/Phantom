package phantom.mvc.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 分页查询数据,所有分页查询接口均需使用此类型作为包装.
 * @author Frodez
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页查询数据")
public class PageData<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 页码数
	 */
	@Getter
	@NotNull
	@PositiveOrZero
	@ApiModelProperty(value = "页码数")
	private int pageNum;

	/**
	 * 单页容量
	 */
	@Getter
	@NotNull
	@Positive
	@ApiModelProperty(value = "单页容量")
	private int pageSize;

	/**
	 * 查询总数
	 */
	@Getter
	@NotNull
	@PositiveOrZero
	@ApiModelProperty(value = "查询总数")
	private long total;

	/**
	 * 分页数据
	 */
	@Getter
	@NotNull
	@ApiModelProperty(value = "分页数据")
	private Collection<T> page;

}
