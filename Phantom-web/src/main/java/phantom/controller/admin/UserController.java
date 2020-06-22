package phantom.controller.admin;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phantom.aop.request.annotation.RepeatLock;
import phantom.dao.model.result.UserInfo;
import phantom.mvc.data.Result.Value;
import phantom.service.admin.IUserService;

@RepeatLock
@RestController
@RequestMapping(value = "/user", name = "用户控制器")
public class UserController {

	@Autowired
	private IUserService userService;

	@GetMapping(value = "/info", name = "获取用户基本信息接口")
	public Value<UserInfo> getUserInfo(@ApiParam("用户名") String username) {
		return userService.getUser(username);
	}

}
