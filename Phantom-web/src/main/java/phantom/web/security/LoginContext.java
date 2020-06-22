package phantom.web.security;

import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import phantom.common.UEmpty;
import phantom.dao.model.User;
import phantom.mvc.UServlet;

@UtilityClass
public class LoginContext {

	private static ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();

	/**
	 * 存储token和user
	 * @author Frodez
	 */
	public static void save(String token, User user) {
		userMap.put(token, user);
	}

	/**
	 * 获取当前user
	 * @author Frodez
	 */
	public static User get() {
		String token = TokenUtil.getRealToken(UServlet.request());
		return UEmpty.no(token) ? userMap.get(token) : null;
	}

	/**
	 * 删除token和对应user
	 * @author Frodez
	 */
	public static void remove(String token) {
		userMap.remove(token);
	}

}
