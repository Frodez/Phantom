package phantom.dao.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import phantom.dao.model.Tag;
import phantom.mybatis.mapper.DataMapper;
import phantom.mybatis.result.CustomResultHandler;
import phantom.mybatis.result.handler.MapResultHandler;

/**
 * @description 标签表
 * @table tb_tag
 */
@Repository
public interface TagMapper extends DataMapper<Tag> {

	@CustomResultHandler(MapResultHandler.class)
	Map<Long, List<String>> selectTags(@Param("articleIds") List<Long> articleIds);

	List<String> selectTagsById(@Param("articleId") Long articleId);

}
