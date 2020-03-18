package phantom.code.checker;

import phantom.common.UString;

/**
 * 代码检查异常
 * @author Frodez
 */
public class CodeCheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CodeCheckException(String... message) {
		super(UString.concat(message), null, false, false);
	}

	public CodeCheckException() {
		super(null, null, false, false);
	}

}
