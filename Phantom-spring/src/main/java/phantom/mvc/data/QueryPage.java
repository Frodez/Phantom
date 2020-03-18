package phantom.mvc.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.session.RowBounds;
import phantom.mvc.data.Result.Value;

/**
 * 通用分页查询请求参数<br>
 * 本参数用于外部接口通信,出于安全考虑,限制了参数的取值范围。<br>
 * 如需无限制地进行分页查询,请使用RowBounds。<br>
 * @see org.apache.ibatis.session#RowBounds
 * @author Frodez
 */
@ToString
@EqualsAndHashCode
@ApiModel(description = "分页查询参数")
public class QueryPage implements IPage, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 默认RowBounds
	 */
	public static final RowBounds ROW_BOUNDS = new RowBounds(DefPage.NUM, DefPage.SIZE);

	/**
	 * 默认QueryPage<br>
	 * 等同于 new QueryPage();<br>
	 */
	public static final QueryPage QUERY_PAGE = new QueryPage(DefPage.NUM, DefPage.SIZE);

	private static final RowBounds NO_PAGE = new RowBounds();

	/**
	 * 使用默认值
	 * @author Frodez
	 */
	public QueryPage() {
		this.pageNum = DefPage.NUM;
		this.pageSize = DefPage.SIZE;
	}

	/**
	 * 构造函数<br>
	 * pageNum为默认值
	 * @author Frodez
	 */
	public QueryPage(Integer pageSize) {
		this.pageNum = DefPage.NUM;
		this.pageSize = pageSize;
	}

	/**
	 * 构造函数
	 * @author Frodez
	 */
	public QueryPage(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	/**
	 * 页码数
	 */
	@Getter
	@NotNull
	@Min(0)
	@ApiModelProperty(value = "页码数,为0时查询全部")
	private Integer pageNum;

	/**
	 * 单页容量
	 */
	@Getter
	@NotNull
	@Min(1)
	@Max(DefPage.MAX_SIZE)
	@ApiModelProperty(value = "单页容量")
	private Integer pageSize;

	/**
	 * <strong>警告!不要使用本方法!本方法永远返回null!</strong><br>
	 * <strong>请使用其他方式设置orderBy!<strong>
	 * @author Frodez
	 */
	@Override
	@JsonIgnore
	public String getOrderBy() {
		return null;
	}

	/**
	 * 转换为RowBounds
	 * @author Frodez
	 */
	public RowBounds toRowBounds() {
		if (pageNum == 0) {
			return NO_PAGE;
		}
		return new RowBounds((pageNum - 1) * pageSize, pageSize);
	}

	/**
	 * 开始分页查询<br>
	 * <strong>请严格遵循PageHelper插件的使用方式!</strong>
	 * @see <url>https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md</url>
	 * @author Frodez
	 */
	public <E> Page<E> page(Supplier<List<E>> supplier) {
		PageHelper.startPage(this);
		return (Page<E>) supplier.get();
	}

	/**
	 * 开始分页查询<br>
	 * <strong>请严格遵循PageHelper插件的使用方式!</strong>
	 * @see <url>https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md</url>
	 * @author Frodez
	 */
	public <E> Value<PageData<E>> start(Supplier<List<E>> supplier) {
		PageHelper.startPage(this);
		return Result.page((Page<E>) supplier.get());
	}

	@UtilityClass
	public static class DefPage {

		/**
		 * 默认页码数
		 */
		public static final int NUM = 0;

		/**
		 * 默认单页容量
		 */
		public static final int SIZE = 20;

		/**
		 * 默认最大单页容量
		 */
		public static final int MAX_SIZE = 1000;

	}

}
