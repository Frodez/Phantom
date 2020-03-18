package phantom.aop.request.advisor;

import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import phantom.common.UString;
import phantom.mvc.UServlet;

/**
 * 请求限流工具类
 * @author Frodez
 */
@UtilityClass
class Util {

	/**
	 * 获取key
	 * @author Frodez
	 */
	public static String servletKey(String sault, HttpServletRequest request) {
		return UString.concat(sault, ":", request.getRequestURI(), ":", UServlet.getAddr(request));
	}

}
