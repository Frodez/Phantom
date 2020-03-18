package phantom.mybatis.mapper.exist;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface ExistMapper {

	/**
	 * 通过id批量查询是否每个id都存在对应的值<br>
	 * <strong>如果每个id都存在对应值,则返回true,否则则返回false</strong>
	 * @param ids 必须是id的对应字段的值!!!
	 * @author Frodez
	 */
	@SelectProvider(type = ExistMapperProvider.class, method = "dynamicSQL")
	boolean existByIds(@Param("ids") List<?> ids);

	/**
	 * 通过equal条件查询是否存在,只支持一个equal条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 */
	@SelectProvider(type = ExistMapperProvider.class, method = "dynamicSQL")
	boolean existEq(@Param("paramName") String paramName, @Param("param") Object param);

	/**
	 * 通过in条件查询是否存在,只支持一个equal条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 */
	@SelectProvider(type = ExistMapperProvider.class, method = "dynamicSQL")
	boolean existIn(@Param("paramName") String paramName, @Param("params") List<?> params);

}
