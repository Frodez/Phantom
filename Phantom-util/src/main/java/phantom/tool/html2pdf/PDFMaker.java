package phantom.tool.html2pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import java.io.ByteArrayOutputStream;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * pdf生成器
 * @author Frodez
 */
@UtilityClass
public class PDFMaker {

	private static DefaultFontProvider defaultFontProvider = new DefaultFontProvider(false, false, true);

	/**
	 * 将html转换为pdf
	 * @author Frodez
	 */
	@SneakyThrows
	public static ByteArrayOutputStream convert(String html) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ConverterProperties properties = new ConverterProperties();
		properties.setFontProvider(defaultFontProvider);
		PdfDocument pdf = new PdfDocument(new PdfWriter(stream));
		Document document = HtmlConverter.convertToDocument(html, pdf, properties);
		document.close();
		return stream;
	}

}
