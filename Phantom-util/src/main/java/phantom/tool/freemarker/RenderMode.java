package phantom.tool.freemarker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import phantom.tool.freemarker.reverter.CSSReverter;
import phantom.tool.freemarker.reverter.Reverter;

/**
 * 转换器类型
 * @author Frodez
 */
@Getter
@AllArgsConstructor
public enum RenderMode {

	CSSREVERTER(new CSSReverter());

	private Reverter reverter;

}
