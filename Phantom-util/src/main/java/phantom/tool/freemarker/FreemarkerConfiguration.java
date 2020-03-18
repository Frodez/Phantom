package phantom.tool.freemarker;

import lombok.Data;

@Data
public class FreemarkerConfiguration {

	/**
	 * 模板根目录
	 */
	private String loaderPath;

	/**
	 * 模板文件后缀
	 */
	private String suffix;

}
