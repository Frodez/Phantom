package phantom.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import phantom.dao.model.Comment;
import phantom.dao.model.result.CommentInfo;
import phantom.dao.param.GetComment;
import phantom.mybatis.mapper.DataMapper;

/**
 * @description 评论表
 * @table tb_comment
 */
@Repository
public interface CommentMapper extends DataMapper<Comment> {

	List<CommentInfo> selectCommentInfo(@Param("param") GetComment param);

}
