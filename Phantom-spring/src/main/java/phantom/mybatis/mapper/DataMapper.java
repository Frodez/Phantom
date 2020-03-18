package phantom.mybatis.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 基础Mapper
 * @author Frodez
 */
@RegisterMapper
public interface DataMapper<T> extends Mapper<T>, MySqlMapper<T>, NormalCustomMapper<T> {

}
