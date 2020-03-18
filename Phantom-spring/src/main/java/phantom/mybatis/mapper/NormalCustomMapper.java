package phantom.mybatis.mapper;

import phantom.mybatis.mapper.count.CountMapper;
import phantom.mybatis.mapper.equal.EqualMapper;
import phantom.mybatis.mapper.exist.ExistMapper;
import phantom.mybatis.mapper.ids.IdsMapper;
import phantom.mybatis.mapper.in.InMapper;
import phantom.mybatis.mapper.partial.SelectPartialMapper;

/**
 * 标准自定义mapper方法
 * @author Frodez
 */
public interface NormalCustomMapper<T> extends IdsMapper<T>, EqualMapper<T>, InMapper<T>, SelectPartialMapper, CountMapper, ExistMapper {

}
