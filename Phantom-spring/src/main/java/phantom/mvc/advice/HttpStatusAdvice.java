package phantom.mvc.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import phantom.mvc.data.Result;
import phantom.mvc.data.Result.Value;

/**
 * 正常返回信息处理
 * @author Frodez
 */
@ControllerAdvice
public class HttpStatusAdvice implements ResponseBodyAdvice<Value<?>> {

	/**
	 * 设置适用范围
	 * @author Frodez
	 */
	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.getMethod().getReturnType() == Result.class;
	}

	/**
	 * 返回前设置http状态码
	 * @author Frodez
	 */
	@Override
	public Value<?> beforeBodyWrite(Value<?> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<
		?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		response.setStatusCode(body.httpStatus());
		return body;
	}

}
