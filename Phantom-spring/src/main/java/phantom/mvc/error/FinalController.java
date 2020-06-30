package phantom.mvc.error;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 最终错误处理器<br>
 * 当ErrorHandler无法解决的异常发生时,由此处理器处理异常.
 * @author Frodez
 */
@Controller
@RequestMapping
public class FinalController extends AbstractErrorController {

	private final ErrorProperties errorProperties;

	@Autowired
	public FinalController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
		super(errorAttributes);
		this.errorProperties = serverProperties.getError();
	}

	@Override
	public String getErrorPath() {
		return errorProperties.getPath();
	}

	@RequestMapping("${server.servlet.context-path:${error.path:/error}}")
	public void error(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	}

	protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
		IncludeStacktrace include = this.errorProperties.getIncludeStacktrace();
		if (include == IncludeStacktrace.ALWAYS) {
			return true;
		}
		if (include == IncludeStacktrace.ON_PARAM) {
			return getTraceParameter(request);
		}
		return false;
	}

}
