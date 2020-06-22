package phantom.controller.blog;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phantom.aop.request.annotation.RepeatLock;
import phantom.constant.UserType;
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
import phantom.service.blog.IArticleService;
import phantom.web.security.RequireUser;

@RepeatLock
@RestController
@RequestMapping(value = "/article", name = "文章信息控制器")
public class ArticleController {

	@Autowired
	IArticleService articleService;

	@PostMapping(value = "/list", name = "获取文章列表接口")
	public Value<PageData<ArticleInfo>> getArticles(@RequestBody GetArticle param) {
		return articleService.getArticles(param);
	}

	@PostMapping(value = "/detail", name = "获取文章详情接口")
	public Value<ArticleDetail> getArticle(@ApiParam("文章ID") Long articleId) {
		return articleService.getArticle(articleId);
	}

	@PostMapping(value = "/comments", name = "获取文章评论接口")
	public Value<PageData<CommentInfo>> getComments(@RequestBody GetComment param) {
		return articleService.getComments(param);
	}

	@RequireUser({ UserType.ADMIN, UserType.NORMAL })
	@PostMapping(value = "", name = "发表文章接口")
	public State postArticle(@RequestBody PostArticle param) {
		return articleService.postArticle(param);
	}

	@RequireUser({ UserType.ADMIN, UserType.NORMAL })
	@PostMapping(value = "/comment", name = "发表评论接口")
	public State postComment(@RequestBody PostComment param) {
		return articleService.postComment(param);
	}

}
