package phantom.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

/**
 * Decimal工具类
 * @author Frodez
 */
@UtilityClass
public class UDecimal {

	/**
	 * 默认Decimal精度
	 */
	public static final int PRECISION = 2;

	/**
	 * 默认Decimal舍入模式
	 */
	public static final RoundingMode ROUND_MODE = RoundingMode.UP;

	/**
	 * 标准化
	 * @author Frodez
	 */
	public static BigDecimal normalize(BigDecimal decimal) {
		return decimal.setScale(PRECISION, ROUND_MODE);
	}

	/**
	 * 标准化
	 * @author Frodez
	 */
	public static BigDecimal normalize(int value) {
		return new BigDecimal(value).setScale(PRECISION, ROUND_MODE);
	}

	/**
	 * 标准化
	 * @author Frodez
	 */
	public static BigDecimal normalize(double value) {
		return new BigDecimal(value).setScale(PRECISION, ROUND_MODE);
	}

	/**
	 * 标准化
	 * @author Frodez
	 */
	public static BigDecimal normalize(long value) {
		return new BigDecimal(value).setScale(PRECISION, ROUND_MODE);
	}

	/**
	 * 标准化
	 * @author Frodez
	 */
	public static BigDecimal normalize(String value) {
		return new BigDecimal(value).setScale(PRECISION, ROUND_MODE);
	}

	/**
	 * 批量相加并标准化,第一个数为被加数
	 * @author Frodez
	 */
	public static BigDecimal add(BigDecimal first, BigDecimal... args) {
		return add(true, first, args);
	}

	/**
	 * 批量相加,第一个数为被加数
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 */
	public static BigDecimal add(boolean normalized, BigDecimal first, BigDecimal... args) {
		if (UEmpty.yes(args)) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = first;
		for (int i = 0; i < args.length; ++i) {
			result = result.add(args[i]);
		}
		return normalized ? UDecimal.normalize(result) : result;
	}

	/**
	 * 批量相减并标准化,第一个数为被减数
	 * @author Frodez
	 */
	public static BigDecimal subtract(BigDecimal first, BigDecimal... args) {
		return subtract(true, first, args);
	}

	/**
	 * 批量相减,第一个数为被减数
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 */
	public static BigDecimal subtract(boolean normalized, BigDecimal first, BigDecimal... args) {
		if (UEmpty.yes(args)) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = first;
		for (int i = 0; i < args.length; ++i) {
			result = result.subtract(args[i]);
		}
		return normalized ? UDecimal.normalize(result) : result;
	}

	/**
	 * 批量相乘并标准化,第一个数为被乘数
	 * @author Frodez
	 */
	public static BigDecimal multiply(BigDecimal first, BigDecimal... args) {
		return multiply(true, first, args);
	}

	/**
	 * 批量相乘,第一个数为被乘数
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 */
	public static BigDecimal multiply(boolean normalized, BigDecimal first, BigDecimal... args) {
		if (UEmpty.yes(args)) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = first;
		for (int i = 0; i < args.length; ++i) {
			result = result.multiply(args[i]);
		}
		return normalized ? UDecimal.normalize(result) : result;
	}

	/**
	 * 批量相除并标准化,第一个数为被除数
	 * @author Frodez
	 */
	public static BigDecimal divide(BigDecimal first, BigDecimal... args) {
		return divide(true, first, args);
	}

	/**
	 * 批量相除,第一个数为被除数
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 */
	public static BigDecimal divide(boolean normalized, BigDecimal first, BigDecimal... args) {
		if (UEmpty.yes(args)) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = first;
		BigDecimal divider = args[0];
		for (int i = 0; i < args.length; ++i) {
			divider = divider.multiply(args[i]);
		}
		result = result.divide(divider, ROUND_MODE);
		return normalized ? UDecimal.normalize(result) : result;
	}

}
