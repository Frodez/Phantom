package phantom.common;

import lombok.experimental.UtilityClass;

/**
 * 布尔逻辑工具类
 * @author Frodez
 */
@UtilityClass
public class UBool {

	/**
	 * 与<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 1<br>
	 * @author Frodez
	 */
	public static boolean and(boolean a, boolean b) {
		return a && b;
	}

	/**
	 * 与<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 1<br>
	 * @author Frodez
	 */
	public static boolean and(Boolean a, Boolean b) {
		return a && b;
	}

	/**
	 * 与非<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 0<br>
	 * @author Frodez
	 */
	public static boolean nand(boolean a, boolean b) {
		return !and(a, b);
	}

	/**
	 * 与非<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 0<br>
	 * @author Frodez
	 */
	public static boolean nand(Boolean a, Boolean b) {
		return !and(a, b);
	}

	/**
	 * 或<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 1<br>
	 * @author Frodez
	 */
	public static boolean or(boolean a, boolean b) {
		return a || b;
	}

	/**
	 * 或<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 1<br>
	 * @author Frodez
	 */
	public static boolean or(Boolean a, Boolean b) {
		return a || b;
	}

	/**
	 * 或非<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 0<br>
	 * @author Frodez
	 */
	public static boolean nor(boolean a, boolean b) {
		return !or(a, b);
	}

	/**
	 * 或非<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 0<br>
	 * @author Frodez
	 */
	public static boolean nor(Boolean a, Boolean b) {
		return !or(a, b);
	}

	/**
	 * 异或<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 0<br>
	 * @author Frodez
	 */
	public static boolean xor(boolean a, boolean b) {
		return a ^ b;
	}

	/**
	 * 异或<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 0<br>
	 * @author Frodez
	 */
	public static boolean xor(Boolean a, Boolean b) {
		return a ^ b;
	}

	/**
	 * 异或非<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 1<br>
	 * @author Frodez
	 */
	public static boolean xnor(boolean a, boolean b) {
		return !xor(a, b);
	}

	/**
	 * 异或非<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 1<br>
	 * @author Frodez
	 */
	public static boolean xnor(Boolean a, Boolean b) {
		return !xor(a, b);
	}

}
