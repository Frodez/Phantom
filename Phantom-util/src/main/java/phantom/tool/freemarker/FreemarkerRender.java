package phantom.tool.freemarker;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.SneakyThrows;
import phantom.common.UEmpty;
import phantom.common.UString;

/**
 * 模板引擎渲染工具类<br>
 * @author Frodez
 */
public class FreemarkerRender {

	private static Configuration configuration;

	@Getter
	private static FreemarkerConfiguration properties;

	/**
	 * 必须初始化完毕后才能使用!
	 * @author Frodez
	 */
	@SneakyThrows
	public static void init(FreemarkerConfiguration properties) {
		configuration = new Configuration(Configuration.getVersion());
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateLoader(loader(properties.getLoaderPath()));
		FreemarkerRender.properties = properties;
	}

	@SneakyThrows
	private static TemplateLoader loader(String loaderPath) {
		try {
			Class<?> resourceLoaderClass = Class.forName("org.springframework.core.io.DefaultResourceLoader");
			Object resourceLoader = resourceLoaderClass.getConstructor().newInstance();
			try {
				Object resource = resourceLoaderClass.getMethod("getResource", String.class).invoke(resourceLoader, loaderPath);
				File file = (File) Class.forName("org.springframework.core.io.Resource").getMethod("getFile").invoke(resource);
				return new FileTemplateLoader(file);
			} catch (Exception e) {
				Class<?> templateLoaderClass = Class.forName("org.springframework.ui.freemarker.SpringTemplateLoader");
				Object loader = templateLoaderClass.getConstructor(resourceLoaderClass, String.class).newInstance(resourceLoader, loaderPath);
				return (TemplateLoader) loader;
			}
		} catch (Exception e) {
			return new FileTemplateLoader(new File(loaderPath));
		}
	}

	private static String revert(String html, RenderMode... modes) {
		if (UEmpty.no(modes)) {
			for (RenderMode mode : modes) {
				html = mode.getReverter().revert(html);
			}
		}
		return html;
	}

	/**
	 * 获取freemarker的执行引擎
	 * @author Frodez
	 */
	public static Configuration configuration() {
		return configuration;
	}

	/**
	 * 渲染页面,并转变为String(默认内联css)
	 * @author Frodez
	 */
	public static String render(String templateName) {
		return render(templateName, new HashMap<>(), RenderMode.CSSREVERTER);
	}

	/**
	 * 渲染页面,并转变为String。可选择添加不同的渲染模式。<br>
	 * 默认无任何需要参数。
	 * @author Frodez
	 */
	public static String render(String templateName, RenderMode... modes) {
		return render(templateName, new HashMap<>(), modes);
	}

	/**
	 * 渲染页面,并转变为String。可选择添加不同的渲染模式。
	 * @param params freemark页面可能需要的参数
	 * @author Frodez
	 */
	@SneakyThrows
	public static String render(String templateName, Map<String, Object> params, RenderMode... modes) {
		if (UEmpty.yes(templateName)) {
			throw new IllegalArgumentException();
		}
		StringWriter writer = new StringWriter();
		configuration.getTemplate(UString.concat(templateName, properties.getSuffix())).process(params, writer);
		return revert(writer.toString(), modes);
	}

}
