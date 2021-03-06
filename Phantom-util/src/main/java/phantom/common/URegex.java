package phantom.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * 正则表达式工具类<br>
 * <strong>建议只有在无法确定正则表达式的内容,且正则表达式的数量不算太多时,才使用本工具类。</strong><br>
 * 如果能确定正则表达式的内容,建议直接编译正则表达式然后使用。<br>
 * 本工具类对编译的正则表达式进行了缓存,校验时需要先查询缓存,显然如果正则表达式内容确定,直接编译并使用的速度更快。<br>
 * 同时,如果正则表达式的数量无法确定或者太多,也可能造成缓存容量太大,降低性能。
 * @author Frodez
 */
@UtilityClass
public class URegex {

	private static final Map<String, Pattern> CACHE = new ConcurrentHashMap<>();

	/**
	 * 是否通过正则表达式校验
	 * @author Frodez
	 */
	public static boolean match(String regex, String input) {
		return CACHE.computeIfAbsent(regex, i -> Pattern.compile(regex)).matcher(input).matches();
	}

	/**
	 * 是否通过正则表达式校验
	 * @author Frodez
	 */
	public static boolean match(String regex, String input, int flag) {
		if (flag == 0) {
			return match(regex, input);
		}
		return CACHE
				.computeIfAbsent(UString.concat(regex, ":", Integer.toString(flag)), i -> Pattern.compile(regex, flag))
				.matcher(input).matches();
	}

	/**
	 * 是否通过正则表达式校验
	 * @see java.util.regex.Pattern
	 * @author Frodez
	 */
	public static boolean match(String regex, String input, int... flags) {
		return match(regex, input, transfer(flags));
	}

	/**
	 * 转换枚举
	 * @see java.util.regex.Pattern
	 * @author Frodez
	 */
	public static int transfer(int... flags) {
		if (UEmpty.yes(flags)) {
			throw new IllegalArgumentException();
		}
		int flag = 0;
		for (int item : flags) {
			flag = flag | item;
		}
		return flag;
	}

}
