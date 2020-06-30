package phantom.service.blog;

import com.github.pagehelper.Page;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import phantom.common.UEmpty;
import phantom.constant.Constant;
import phantom.dao.mapper.ArticleMapper;
import phantom.dao.mapper.CategoryMapper;
import phantom.dao.mapper.CommentMapper;
import phantom.dao.mapper.TagMapper;
import phantom.dao.model.Article;
import phantom.dao.model.Comment;
import phantom.dao.model.Tag;
import phantom.dao.model.User;
import phantom.dao.model.result.ArticleDetail;
import phantom.dao.model.result.ArticleInfo;
import phantom.dao.model.result.CommentInfo;
import phantom.dao.param.GetArticle;
import phantom.dao.param.GetComment;
import phantom.dao.param.PostArticle;
import phantom.dao.param.PostComment;
import phantom.mvc.UServlet;
import phantom.mvc.data.PageData;
import phantom.mvc.data.Result;
import phantom.mvc.data.Result.State;
import phantom.mvc.data.Result.Value;
import phantom.reflect.UBean;
import phantom.tool.jackson.JSON;
import phantom.web.security.LoginContext;
import phantom.web.security.TokenUtil;

@Slf4j
@Component
public class ArticleService implements IArticleService {

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private TagMapper tagMapper;

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public Value<PageData<ArticleInfo>> getArticles(GetArticle param) {
		Page<ArticleInfo> page = param.getPage().page(() -> articleMapper.selectArticleInfo(param));
		List<ArticleInfo> result = page.getResult();
		if (UEmpty.no(result)) {
			Map<Long, List<String>> tagMap = tagMapper
					.selectTags(result.stream().map(ArticleInfo::getId).collect(Collectors.toList()));
			for (ArticleInfo info : result) {
				info.setTags(tagMap.get(info.getId()));
			}
		}
		return Result.page(page, result);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Value<ArticleDetail> getArticle(Long articleId) {
		ArticleDetail article = articleMapper.selectArticleDetail(articleId);
		if (article == null) {
			return Result.fail("该文章不存在");
		}
		article.setTags(tagMapper.selectTagsById(articleId));
		return Result.success(article);
	}

	@Override
	public Value<PageData<CommentInfo>> getComments(GetComment param) {
		return param.getPage().start(() -> commentMapper.selectCommentInfo(param));
	}

	@Override
	public State postArticle(PostArticle param) {
		User user = LoginContext.get();
		if (user == null) {
			log.info("token:{},postArticle:{}", TokenUtil.getRealToken(UServlet.request()), JSON.string(param));
			return Result.notLogin();
		}
		if (!categoryMapper.existsWithPrimaryKey(param.getCategoryId())) {
			return Result.fail("分类不存在");
		}
		Article article = UBean.copy(param, Article::new);
		article.setAuthorId(user.getId());
		Date date = new Date();
		article.setCreateTime(date);
		article.setUpdateTime(date);
		articleMapper.insertUseGeneratedKeys(article);
		if (UEmpty.no(param.getTags())) {
			List<Tag> tags = param.getTags().stream().map((item) -> {
				Tag tag = new Tag();
				tag.setArticleId(article.getId());
				tag.setTag(item);
				return tag;
			}).collect(Collectors.toList());
			tagMapper.insertList(tags);
		}
		return Result.success();
	}

	@Override
	public State postComment(PostComment param) {
		User user = LoginContext.get();
		if (user == null) {
			log.info("token:{},postArticle:{}", TokenUtil.getRealToken(UServlet.request()), JSON.string(param));
			return Result.notLogin();
		}
		Comment comment = UBean.copy(param, Comment::new);
		comment.setCreateTime(new Date());
		comment.setUserId(param.getAnonymous() ? Constant.ANONYMOUS_ID : user.getId());
		commentMapper.insert(comment);
		return Result.success();
	}

}
