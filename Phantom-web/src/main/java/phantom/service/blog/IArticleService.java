package phantom.service.blog;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import phantom.dao.model.result.ArticleDetail;
import phantom.dao.model.result.ArticleInfo;
import phantom.dao.model.result.CommentInfo;
import phantom.dao.param.GetArticle;
import phantom.dao.param.GetComment;
import phantom.dao.param.PostArticle;
import phantom.dao.param.PostComment;
import phantom.mvc.data.PageData;
import phantom.mvc.data.Result.State;
import phantom.mvc.data.Result.Value;
import phantom.tool.validate.annotation.Check;

/**
 * 文章服务
 * @author Frodez
 */
@Check
public interface IArticleService {

	/**
	 * 获取文章列表
	 * @author Frodez
	 */
	Value<PageData<ArticleInfo>> getArticles(@Valid @NotNull GetArticle param);

	/**
	 * 获取文章详情
	 * @author Frodez
	 */
	Value<ArticleDetail> getArticle(@NotNull Long articleId);

	/**
	 * 获取评论
	 * @author Frodez
	 */
	Value<PageData<CommentInfo>> getComments(@Valid @NotNull GetComment param);

	/**
	 * 发表文章
	 * @author Frodez
	 */
	@Transactional
	State postArticle(@Valid @NotNull PostArticle param);

	/**
	 * 发表评论
	 * @author Frodez
	 */
	@Transactional
	State postComment(@Valid @NotNull PostComment param);

}
