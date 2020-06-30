package phantom.service.admin;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;

import phantom.dao.model.result.UserInfo;
import phantom.dao.param.Identifier;
import phantom.dao.param.Logout;
import phantom.dao.param.Register;
import phantom.mvc.data.Result.State;
import phantom.mvc.data.Result.Value;
import phantom.tool.validate.annotation.Check;

@Check
public interface IUserService {

	/**
	 * 注册
	 * @author Frodez
	 */
	@Transactional
	State register(@Valid @NotNull Register param);

	/**
	 * 登录
	 * @author Frodez
	 */
	Value<String> login(@Valid @NotNull Identifier param);

	/**
	 * 登出
	 * @author Frodez
	 */
	State logout(@Valid @NotNull Logout param);

	/**
	 * 获取用户信息
	 * @author Frodez
	 */
	Value<UserInfo> getUser(@NotBlank @Length(max = 255) String username);

}
