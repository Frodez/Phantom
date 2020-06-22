package phantom.mvc.advice;

import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import phantom.mvc.UServlet;
import phantom.mvc.data.Result;

/***
 * 错误信息处理
 * @author Frodez
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param Exception 异常
	 * @author Frodez
	 */
	@ExceptionHandler(value = Exception.class)
	public void exceptionHandler(HttpServletResponse response, Exception e) {
		log.error("Exception", e);
		UServlet.writeJson(response, Result.errorService());
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param HttpMessageConversionException 异常
	 * @author Frodez
	 */
	@ExceptionHandler(value = HttpMessageConversionException.class)
	public void exceptionHandler(HttpServletResponse response, HttpMessageConversionException e) {
		log.error("HttpMessageConversionException", e);
		UServlet.writeJson(response, Result.errorRequest());
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param ServletRequestBindingException 异常
	 * @author Frodez
	 */
	@ExceptionHandler(value = ServletRequestBindingException.class)
	public void exceptionHandler(HttpServletResponse response, ServletRequestBindingException e) {
		log.error("ServletRequestBindingException", e);
		UServlet.writeJson(response, Result.errorRequest());
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param ServletRequestBindingException 异常
	 * @author Frodez
	 */
	@ExceptionHandler(value = AsyncRequestTimeoutException.class)
	public void exceptionHandler(HttpServletResponse response, AsyncRequestTimeoutException e) {
		log.error("AsyncRequestTimeoutException", e);
		UServlet.writeJson(response, Result.busy());
	}

}
