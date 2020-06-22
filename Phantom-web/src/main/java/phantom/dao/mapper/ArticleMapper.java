package phantom.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import phantom.dao.model.Article;
import phantom.dao.model.result.ArticleDetail;
import phantom.dao.model.result.ArticleInfo;
import phantom.dao.param.GetArticle;
import phantom.mybatis.mapper.DataMapper;

/**
 * @description 文章表
 * @table tb_article
 */
@Repository
public interface ArticleMapper extends DataMapper<Article> {

	ArticleDetail selectArticleDetail(@Param("articleId") Long articleId);

	List<ArticleInfo> selectArticleInfo(@Param("param") GetArticle param);

}
