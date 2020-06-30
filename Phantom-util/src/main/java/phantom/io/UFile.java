package phantom.io;

import com.google.common.collect.ImmutableList;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import java.io.File;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件工具类<br>
 * 本工具类主要负责文件到字符,字节的读取和转换<br>
 * 由于路径处理方面的原因,建议在spring体系下读取File使用ResourceUtils,然后使用本工具类转换。<br>
 * @see org.springframework.util.ResourceUtils
 * @author Frodez
 */
@Slf4j
@UtilityClass
public class UFile {

	/**
	 * 项目根路径,前面未加协议名。如果要转换为URI,请在前面加上协议名(如file)。
	 */
	public static final String PATH = UFile.class.getClassLoader().getResource("").getPath();

	/**
	 * 位于spring环境下时,org.springframework.util.ResourceUtils.getFile(String)方法的句柄
	 */
	private static MethodHandle getFile;

	static {
		try {
			Class<?> klass = Class.forName("org.springframework.util.ResourceUtils");
			getFile = MethodHandles.lookup().unreflect(klass.getDeclaredMethod("getFile", String.class));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 当位于spring环境下时,实际上使用spring的org.springframework.util.ResourceUtils.getFile(String)方法<br>
	 * 否则直接使用new File(String)
	 * @author Frodez
	 */
	@SneakyThrows
	public static File getFile(String fileName) {
		return getFile != null ? (File) getFile.invoke(fileName) : new File(fileName);
	}

	/**
	 * 读取文件数据到字符串,默认UTF-8
	 * @author Frodez
	 */
	@SneakyThrows
	public static String readString(File file) {
		return readString(file, StandardCharsets.UTF_8);
	}

	/**
	 * 读取文件数据到字符串,需指定Charset
	 * @author Frodez
	 */
	@SneakyThrows
	public static String readString(File file, Charset charset) {
		return Files.asCharSource(file, charset).read();
	}

	/**
	 * 读取文件数据到字符串List,默认UTF-8<br>
	 * <strong>该字符串不可变,如需可变,需要将其转化为可变List。</strong>
	 * @author Frodez
	 */
	@SneakyThrows
	public static ImmutableList<String> readStrings(File file) {
		return readStrings(file, StandardCharsets.UTF_8);
	}

	/**
	 * 读取文件数据到字符串List,需指定Charset<br>
	 * <strong>该字符串不可变,如需可变,需要将其转化为可变List。</strong>
	 * @author Frodez
	 */
	@SneakyThrows
	public static ImmutableList<String> readStrings(File file, Charset charset) {
		return Files.asCharSource(file, charset).readLines();
	}

	/**
	 * 以UTF-8格式向文件写入字符串,默认覆盖原文件
	 * @author Frodez
	 */
	@SneakyThrows
	public static void writeString(String content, File file) {
		writeString(content, file, false);
	}

	/**
	 * 以UTF-8格式向文件写入字符串,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 */
	@SneakyThrows
	public static void writeString(String content, File file, boolean isAppend) {
		if (isAppend) {
			Files.asCharSink(file, StandardCharsets.UTF_8, FileWriteMode.APPEND).write(content);
		} else {
			Files.asCharSink(file, StandardCharsets.UTF_8).write(content);
		}
	}

	/**
	 * 以UTF-8格式向文件写入字符串,默认覆盖原文件,需指定Charset
	 * @author Frodez
	 */
	@SneakyThrows
	public static void writeString(String content, File file, Charset charset) {
		writeString(content, file, charset, false);
	}

	/**
	 * 以UTF-8格式向文件写入字符串,需指定Charset,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 */
	@SneakyThrows
	public static void writeString(String content, File file, Charset charset, boolean isAppend) {
		if (isAppend) {
			Files.asCharSink(file, charset, FileWriteMode.APPEND).write(content);
		} else {
			Files.asCharSink(file, charset).write(content);
		}
	}

	/**
	 * 读取文件数据到byte数组
	 * @author Frodez
	 */
	@SneakyThrows
	public static byte[] readBytes(File file) {
		return Files.asByteSource(file).read();
	}

	/**
	 * 向文件写入byte数组,默认覆盖原文件
	 * @author Frodez
	 */
	@SneakyThrows
	public static void writeBytes(byte[] content, File file) {
		writeBytes(content, file, false);
	}

	/**
	 * 向文件写入byte数组,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 */
	@SneakyThrows
	public static void writeBytes(byte[] content, File file, boolean isAppend) {
		if (isAppend) {
			Files.asByteSink(file, FileWriteMode.APPEND).write(content);
		} else {
			Files.asByteSink(file).write(content);
		}
	}

	/**
	 * 将输入流输入数据转入文件中,默认覆盖原文件
	 * @author Frodez
	 */
	@SneakyThrows
	public static void transfer(InputStream input, File file) {
		transfer(input, file, false);
	}

	/**
	 * 将输入流输入数据转入文件中,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 */
	@SneakyThrows
	public static void transfer(InputStream input, File file, boolean isAppend) {
		if (isAppend) {
			Files.asByteSink(file, FileWriteMode.APPEND).writeFrom(input);
		} else {
			Files.asByteSink(file).writeFrom(input);
		}
	}

}
