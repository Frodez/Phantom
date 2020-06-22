package phantom.service.admin;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import phantom.constant.AvailableStatus;
import phantom.constant.UserType;
import phantom.dao.mapper.UserMapper;
import phantom.dao.model.User;
import phantom.dao.model.result.UserInfo;
import phantom.dao.param.Identifier;
import phantom.dao.param.Logout;
import phantom.dao.param.Register;
import phantom.mvc.data.Result;
import phantom.mvc.data.Result.State;
import phantom.mvc.data.Result.Value;
import phantom.reflect.UBean;
import phantom.web.security.LoginContext;
import phantom.web.security.SecretUtil;
import phantom.web.security.TokenCache;
import phantom.web.security.TokenUtil;

@Component
public class UserService implements IUserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private TokenCache tokenCache;

	@Override
	public State register(Register param) {
		//注册流程:
		//1.为密码加密
		//2.保存信息
		if (userMapper.existEq("name", param.getIdentifier().getUsername())) {
			return Result.fail("用户名已注册");
		}
		String password = SecretUtil.generate(param.getIdentifier().getPassword());
		User user = new User();
		user.setCreateTime(new Date());
		user.setName(param.getIdentifier().getUsername());
		user.setPassword(password);
		user.setEmail(param.getEmail());
		user.setStatus(AvailableStatus.YES.getVal());
		user.setType(UserType.NORMAL.getVal());
		userMapper.insert(user);
		return Result.success();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Value<String> login(Identifier param) {
		//登录流程:
		//1.获取用户信息,没有信息返回用户名或密码错误
		//2.对比密码,失败则返回用户名或密码错误
		//3.查找缓存中是否存在原有token,存在则删除
		//4.生成token,放入缓存,并返回token
		User user = userMapper.selectOneEq("name", param.getUsername());
		if (user == null || AvailableStatus.NO.getVal().equals(user.getStatus())) {
			return Result.fail("用户名或密码错误");
		}
		if (!SecretUtil.match(param.getPassword(), user.getPassword())) {
			return Result.fail("用户名或密码错误");
		}
		String oldToken = tokenCache.removeByName(param.getUsername());
		if (oldToken != null) {
			LoginContext.remove(oldToken);
		}
		String token = TokenUtil.generate(param.getUsername(), UserType.of(user.getType()));
		tokenCache.save(param.getUsername(), token);
		LoginContext.save(token, user);
		return Result.success(token);
	}

	@Override
	public State logout(Logout param) {
		//登出流程:
		//1.获取token,没有token则返回用户名或密码错误
		//2.对比用户名和密码,失败则返回用户名或密码错误
		//3.删除缓存中token
		if (!tokenCache.exist(param.getIdentifier().getUsername(), param.getToken())) {
			return Result.fail("用户名或密码错误");
		}
		User user = userMapper.selectOneEq("name", param.getIdentifier().getUsername());
		if (user == null) {
			return Result.fail("用户名或密码错误");
		}
		if (!SecretUtil.match(param.getIdentifier().getPassword(), user.getPassword())) {
			return Result.fail("用户名或密码错误");
		}
		tokenCache.removeByToken(param.getToken());
		LoginContext.remove(param.getToken());
		return Result.success();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Value<UserInfo> getUser(String username) {
		User user = userMapper.selectOneEq("name", username);
		if (user == null) {
			return Result.fail("用户不存在");
		}
		if (AvailableStatus.NO.getVal().equals(user.getStatus())) {
			return Result.fail("用户被禁用");
		}
		return Result.success(UBean.copy(user, UserInfo::new));
	}

}
