package phantom.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.experimental.UtilityClass;

/**
 * 判空工具类<br>
 * 空的定义:<br>
 * 1.对String对象,空为null或者空字符串<br>
 * 2.对数组或者Collection或者Map,空为null或者长度为0<br>
 * 3.对两个Collection,空为不存在相同元素<br>
 * 4.对其他情况,空为null<br>
 * 严格模式:<br>
 * 1.严格模式针对数组或者Collection或者Map,要求数组或者Collection中每个元素均不为null,要求Map中所有key对应value均不为null<br>
 * 2.严格模式默认关闭<br>
 * 方法名说明:<br>
 * yes方法用于判断对象是否为空,no方法用于判断对象是否不为空
 * @author Frodez
 */
@UtilityClass
public class UEmpty {

	public static boolean yes(boolean[] array) {
		return array == null || array.length == 0;
	}

	public static boolean no(boolean[] array) {
		return array != null && array.length != 0;
	}

	public static boolean yes(byte[] array) {
		return array == null || array.length == 0;
	}

	public static boolean no(byte[] array) {
		return array != null && array.length != 0;
	}

	public static boolean yes(char[] array) {
		return array == null || array.length == 0;
	}

	public static boolean no(char[] array) {
		return array != null && array.length != 0;
	}

	public static boolean yes(short[] array) {
		return array == null || array.length == 0;
	}

	public static boolean no(short[] array) {
		return array != null && array.length != 0;
	}

	public static boolean yes(int[] array) {
		return array == null || array.length == 0;
	}

	public static boolean no(int[] array) {
		return array != null && array.length != 0;
	}

	public static boolean yes(long[] array) {
		return array == null || array.length == 0;
	}

	public static boolean no(long[] array) {
		return array != null && array.length != 0;
	}

	public static boolean yes(float[] array) {
		return array == null || array.length == 0;
	}

	public static boolean no(float[] array) {
		return array != null && array.length != 0;
	}

	public static boolean yes(double[] array) {
		return array == null || array.length == 0;
	}

	public static boolean no(double[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空,默认关闭严格模式
	 * @author Frodez
	 */
	public static boolean yes(Object[] array) {
		return yes(false, array);
	}

	/**
	 * 判断对象是否为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 */
	public static boolean yes(boolean strictMode, Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		if (!strictMode) {
			return false;
		}
		for (Object object : array) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否不为空,默认关闭严格模式
	 * @author Frodez
	 */
	public static boolean no(Object[] array) {
		return no(false, array);
	}

	/**
	 * 判断对象是否不为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 */
	public static boolean no(boolean strictMode, Object[] array) {
		if (array == null || array.length == 0) {
			return false;
		}
		if (!strictMode) {
			return true;
		}
		for (Object object : array) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断对象是否为空,默认关闭严格模式
	 * @author Frodez
	 */
	public static boolean yes(Collection<?> collection) {
		return yes(false, collection);
	}

	/**
	 * 判断对象是否为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 */
	public static boolean yes(boolean strictMode, Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		if (!strictMode) {
			return false;
		}
		for (Object object : collection) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否不为空,默认关闭严格模式
	 * @author Frodez
	 */
	public static boolean no(Collection<?> collection) {
		return no(false, collection);
	}

	/**
	 * 判断对象是否不为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 */
	public static boolean no(boolean strictMode, Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}
		if (!strictMode) {
			return true;
		}
		for (Object object : collection) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 返回两个集合是否不存在相同元素
	 * @author Frodez
	 */
	public static <E> boolean yes(Collection<E> a, Collection<E> b) {
		return Collections.disjoint(a, b);
	}

	/**
	 * 返回两个集合是否存在相同元素
	 * @author Frodez
	 */
	public static <E> boolean no(Collection<E> a, Collection<E> b) {
		return !Collections.disjoint(a, b);
	}

	/**
	 * 判断对象是否为空,默认关闭严格模式
	 * @author Frodez
	 */
	public static boolean yes(Map<Object, Object> map) {
		return yes(false, map);
	}

	/**
	 * 判断对象是否为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 */
	public static boolean yes(boolean strictMode, Map<Object, Object> map) {
		if (map == null || map.isEmpty()) {
			return true;
		}
		if (!strictMode) {
			return false;
		}
		for (Object object : map.values()) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否不为空,默认关闭严格模式
	 * @author Frodez
	 */
	public static boolean no(Map<Object, Object> map) {
		return no(false, map);
	}

	/**
	 * 判断对象是否不为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 */
	public static boolean no(boolean strictMode, Map<Object, Object> map) {
		if (map == null || map.isEmpty()) {
			return false;
		}
		if (!strictMode) {
			return true;
		}
		for (Object object : map.values()) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否为空<br>
	 * <strong>警告:与其他方法不同的是,长度为0的空字符串也会被判定为空。这一行为与对数组的判断类似,但并不完全相同。</strong>
	 * @author Frodez
	 */
	public static boolean yes(CharSequence charSequence) {
		return charSequence == null || charSequence.length() == 0;
	}

	/**
	 * 判断字符串是否不为空<br>
	 * <strong>警告:与其他方法不同的是,长度为0的空字符串也会被判定为空。这一行为与对数组的判断类似,但并不完全相同。</strong>
	 * @author Frodez
	 */
	public static boolean no(CharSequence charSequence) {
		return charSequence != null && charSequence.length() != 0;
	}

	/**
	 * 将数组中的null元素剔除,默认关闭严格模式
	 * @author Frodez
	 */
	public static Object[] trim(Object[] objects) {
		return trim(false, objects);
	}

	/**
	 * 将数组中的null元素剔除<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 */
	public static Object[] trim(boolean strictMode, Object[] objects) {
		if (!strictMode) {
			int last = objects.length - 1;
			for (; last >= 0; last--) {
				if (objects[last] != null) {
					break;
				}
			}
			Object[] result = new Object[last + 1];
			System.arraycopy(objects, 0, result, 0, last + 1);
			return result;
		}
		int size = 0;
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				size++;
			}
		}
		if (size == 0) {
			return new Object[0];
		}
		Object[] result = new Object[size];
		for (int i = 0, j = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				result[j] = objects[i];
				j++;
			}
		}
		return result;
	}

	/**
	 * 将数组中的null元素剔除,默认关闭严格模式
	 * @author Frodez
	 */
	public static CharSequence[] trim(CharSequence[] objects) {
		return trim(false, objects);
	}

	/**
	 * 将数组中的null元素剔除<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 */
	public static CharSequence[] trim(boolean strictMode, CharSequence[] objects) {
		if (!strictMode) {
			int last = objects.length - 1;
			for (; last >= 0; last--) {
				if (UEmpty.yes(objects[last])) {
					break;
				}
			}
			CharSequence[] result = new CharSequence[last + 1];
			System.arraycopy(objects, 0, result, 0, last + 1);
			return result;
		}
		int size = 0;
		for (int i = 0; i < objects.length; i++) {
			if (UEmpty.no(objects[i])) {
				size++;
			}
		}
		if (size == 0) {
			return new CharSequence[0];
		}
		CharSequence[] result = new CharSequence[size];
		for (int i = 0, j = 0; i < objects.length; i++) {
			if (UEmpty.no(objects[i])) {
				result[j] = objects[i];
				j++;
			}
		}
		return result;
	}

}
