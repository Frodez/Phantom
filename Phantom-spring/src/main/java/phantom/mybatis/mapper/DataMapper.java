package phantom.mybatis.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 基础Mapper
 * @author Frodez
 */
@RegisterMapper
public interface DataMapper<T> extends BaseMapper<T>, ExampleMapper<T>, MySqlMapper<T>, NormalCustomMapper<T>, Marker {

}
