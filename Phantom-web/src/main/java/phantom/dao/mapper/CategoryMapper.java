package phantom.dao.mapper;

import org.springframework.stereotype.Repository;
import phantom.dao.model.Category;
import phantom.mybatis.mapper.DataMapper;

/**
 * @description 分类表
 * @table tb_category
 */
@Repository
public interface CategoryMapper extends DataMapper<Category> {
}