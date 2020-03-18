package phantom.tool.freemarker.reverter;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import phantom.common.UEmpty;
import phantom.common.UString;
import phantom.io.UFile;
import phantom.tool.freemarker.FreemarkerRender;

/**
 * 外联css转换器<br>
 * 这里没有实现js的外联到内联的转化,因为本转换器的使用场景不是http,而只是内部的转换,不需要执行js。<br>
 * @author Frodez
 */
@Slf4j
public class CSSReverter implements Reverter {

	/**
	 * 将html中外联的css变成内联,并去掉外联样式
	 * @author Frodez
	 */
	@Override
	public String revert(String html) {
		if (UEmpty.yes(html)) {
			throw new IllegalArgumentException();
		}
		try {
			Document document = Jsoup.parse(html);
			Elements links = document.select("link[href]");
			Elements htmlElement = document.select("html");
			for (Element iter : links) {
				String path = iter.attr("href");
				if (!path.endsWith(".css")) {
					continue;
				}
				path = UString.concat(FreemarkerRender.getProperties().getLoaderPath(), path);
				String content = UFile.readString(UFile.getFile(path));
				htmlElement.prepend(UString.concat("<style type=\"text/css\">", content, "</style>"));
			}
			links.remove();
			return document.html();
		} catch (Exception e) {
			log.error(e.getMessage());
			return html;
		}
	}

}
