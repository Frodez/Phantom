package phantom.web.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import phantom.common.Pair;
import phantom.common.UEmpty;
import phantom.constant.UserType;
import phantom.mvc.UServlet;
import phantom.mvc.data.Result;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

	private ConcurrentHashMap<String, Optional<RequireUser>> securityHandles = new ConcurrentHashMap<>();

	@Autowired
	private TokenCache tokenCache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		//非HandlerMethod的默认通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Optional<RequireUser> requireUser = securityHandles.computeIfAbsent(handlerMethod.toString(), (name) -> {
			RequireUser annotation = handlerMethod.getMethodAnnotation(RequireUser.class);
			//没有注解的默认通过
			if (annotation == null) {
				annotation = handlerMethod.getBeanType().getAnnotation(RequireUser.class);
				if (annotation == null) {
					return Optional.empty();
				}
			}
			return Optional.of(annotation);
		});
		if (requireUser.isEmpty()) {
			return true;
		}
		//验证流程:
		//1.必须带有jwt token
		//2.token不能过时。如果过时,要从缓存里删除该token
		//3.如果缓存里没有该token,则返回未登录
		//4.如果缓存里有该token,则判断权限是否满足条件。不满足返回无权限
		//5.满足条件,则通过
		//登录流程:
		//1.获取用户信息,没有信息返回用户名或密码错误
		//2.对比密码,失败则返回用户名或密码错误
		//3.查找缓存中是否存在原有token,存在则删除
		//4.生成token,放入缓存,并返回token
		//登出流程:
		//1.获取token,没有token则返回用户名或密码错误
		//2.对比用户名和密码,失败则返回用户名或密码错误
		//3.删除缓存中token
		//注册流程:
		//1.为密码加密
		//2.保存信息
		UserType[] permits = requireUser.get().value();
		//获取jwt信息
		String token = TokenUtil.getRealToken(request);
		//没有jwt token则不通过
		if (UEmpty.yes(token)) {
			UServlet.writeJson(response, Result.noAuth());
			return false;
		}
		Pair<String, UserType> user;
		try {
			user = TokenUtil.verify(token);
		} catch (TokenExpiredException e) {
			//如果token超时失效,删除token,而是直接返回,并告诉客户端token失效,让客户端重新登陆.
			UServlet.writeJson(response, Result.expired());
			tokenCache.removeByToken(token);
			LoginContext.remove(token);
			return false;
		}
		//检查是否登录
		if (!tokenCache.exist(user.getFirst(), token)) {
			UServlet.writeJson(response, Result.notLogin());
			return false;
		}
		//检查权限
		for (UserType userType : permits) {
			if (userType == user.getSecond()) {
				return true;
			}
		}
		//权限未通过
		UServlet.writeJson(response, Result.noAccess());
		return false;
	}

}
