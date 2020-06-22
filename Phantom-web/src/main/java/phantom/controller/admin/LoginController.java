package phantom.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phantom.aop.request.annotation.TimeoutLock;
import phantom.constant.UserType;
import phantom.dao.param.Identifier;
import phantom.dao.param.Logout;
import phantom.dao.param.Register;
import phantom.mvc.data.Result.State;
import phantom.mvc.data.Result.Value;
import phantom.service.admin.IUserService;
import phantom.web.security.RequireUser;

@TimeoutLock(3000)
@RestController
@RequestMapping(value = "/login", name = "登录控制器")
public class LoginController {

	@Autowired
	private IUserService userService;

	@PostMapping(value = "/register", name = "注册接口")
	public State register(@RequestBody Register param) {
		return userService.register(param);
	}

	@PostMapping(value = "/auth", name = "登录接口")
	public Value<String> login(@RequestBody Identifier param) {
		return userService.login(param);
	}

	@RequireUser({ UserType.ADMIN, UserType.NORMAL })
	@PostMapping(value = "/logout", name = "退出接口")
	public State logout(@RequestBody Logout param) {
		return userService.logout(param);
	}

}
