package phantom.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.experimental.UtilityClass;

/**
 * 日期工具类<br>
 * 包括了对符合默认格式的日期,时间和日期——时间的处理。
 * @author Frodez
 */
@UtilityClass
public class UDate {

	/**
	 * 默认时间差
	 */
	public static final ZoneOffset DEFAULT_OFFSET = OffsetDateTime.now().getOffset();

	/**
	 * 默认日期格式
	 */
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 默认时间格式
	 */
	public static final String TIME_PATTERN = "HH:mm:ss";

	/**
	 * 默认日期——时间格式
	 */
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 默认精细时间格式
	 */
	public static final String PRECISIVE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 默认日期类型格式化器
	 */
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

	/**
	 * 默认时间类型格式化器
	 */
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

	/**
	 * 默认日期——时间类型格式化器
	 */
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

	/**
	 * 默认精细时间类型格式化器
	 */
	public static final DateTimeFormatter PRECISIVE_FORMATTER = DateTimeFormatter.ofPattern(PRECISIVE_PATTERN);

	/**
	 * 将date转换为yyyy-MM-dd格式字符串
	 * @author Frodez
	 */
	public static String dateStr(final Date date) {
		return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()).toString();
	}

	/**
	 * 获取当前时间的yyyy-MM-dd HH:mm:ss格式字符串
	 * @author Frodez
	 */
	public static String nowStr() {
		return LocalDateTime.now().format(DATE_TIME_FORMATTER);
	}

	/**
	 * 获取当前时间的yyyy-MM-dd HH:mm:ss.SSS格式字符串
	 * @author Frodez
	 */
	public static String precisiveStr() {
		return LocalDateTime.now().format(PRECISIVE_FORMATTER);
	}

	/**
	 * 获取当前时间的yyyy-MM-dd格式字符串
	 * @author Frodez
	 */
	public static String todayStr() {
		return LocalDate.now().toString();
	}

	/**
	 * 获取当前日期(时刻为0点0分)
	 * @author Frodez
	 */
	public static Date today() {
		return Date.from(LocalDate.now().atStartOfDay().toInstant(DEFAULT_OFFSET));
	}

	/**
	 * 格式化日期(yyyy-MM-dd格式)
	 * @author Frodez
	 */
	public static Date date(String date) {
		return Date.from(LocalDate.parse(date, DATE_FORMATTER).atStartOfDay().toInstant(DEFAULT_OFFSET));
	}

	/**
	 * 是正确的yyyy-MM-dd格式日期
	 * @author Frodez
	 */
	public static boolean isDate(String date) {
		try {
			LocalDate.parse(date, DATE_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化时间(HH:mm:ss格式)
	 * @author Frodez
	 */
	public static Date time(String date) {
		return Date.from(LocalTime.parse(date, TIME_FORMATTER).atDate(LocalDate.now()).toInstant(DEFAULT_OFFSET));
	}

	/**
	 * 是正确的HH:mm:ss格式日期
	 * @author Frodez
	 */
	public static boolean isTime(String date) {
		try {
			LocalTime.parse(date, TIME_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化日期(yyyy-MM-dd HH:mm:ss格式)
	 * @author Frodez
	 */
	public static Date dateTime(String date) {
		return Date.from(LocalDateTime.parse(date, DATE_TIME_FORMATTER).atOffset(DEFAULT_OFFSET).toInstant());
	}

	/**
	 * 是正确的yyyy-MM-dd HH:mm:ss格式日期
	 * @author Frodez
	 */
	public static boolean isDateTime(String date) {
		try {
			LocalDateTime.parse(date, DATE_TIME_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化日期(yyyy-MM-dd HH:mm:ss.SSS格式)
	 * @author Frodez
	 */
	public static Date precisiveTime(String date) {
		return Date.from(LocalDateTime.parse(date, PRECISIVE_FORMATTER).atOffset(DEFAULT_OFFSET).toInstant());
	}

	/**
	 * 是正确的yyyy-MM-dd HH:mm:ss.SSS格式日期
	 * @author Frodez
	 */
	public static boolean isPrecisiveTime(String date) {
		try {
			LocalDateTime.parse(date, PRECISIVE_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
