package phantom.common;

import lombok.experimental.UtilityClass;

/**
 * 基本类型工具类
 * @author Frodez
 */
@UtilityClass
public class UPrimitive {

	/**
	 * 判断是否为基本类型或者其装箱类
	 * @author Frodez
	 */
	public static boolean isBaseType(Class<?> klass) {
		return klass.isPrimitive() || klass == Boolean.class || klass == Byte.class || klass == Short.class
				|| klass == Character.class
				|| klass == Integer.class || klass == Long.class || klass == Float.class || klass == Double.class;
	}

	/**
	 * 将两个数转换为指定的类型然后进行比较<br>
	 * 涉及类型:byte, short, int, long以及对应装箱类
	 * @author Frodez
	 */
	public static int compare(Object first, Object second, Class<?> baseClass) {
		if (first == null) {
			throw new IllegalArgumentException("first must not be null");
		}
		if (second == null) {
			throw new IllegalArgumentException("second must not be null");
		}
		if (baseClass == Byte.class || baseClass == byte.class) {
			return Byte.compare(cast(first, Byte.class), cast(second, Byte.class));
		} else if (baseClass == Short.class || baseClass == short.class) {
			return Short.compare(cast(first, Short.class), cast(second, Short.class));
		} else if (baseClass == Integer.class || baseClass == int.class) {
			return Integer.compare(cast(first, Integer.class), cast(second, Integer.class));
		} else if (baseClass == Long.class || baseClass == long.class) {
			return Long.compare(cast(first, Long.class), cast(second, Long.class));
		} else if (baseClass == Double.class || baseClass == double.class) {
			return Double.compare(cast(first, Double.class), cast(second, Double.class));
		} else if (baseClass == Float.class || baseClass == float.class) {
			return Float.compare(cast(first, Float.class), cast(second, Float.class));
		} else {
			throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类!");
		}
	}

	/**
	 * 以参数a的类型为标准,判断b是否和a是同一个基本类型或者其装箱类的类型
	 * @author Frodez
	 */
	public static boolean isSameType(Class<?> a, Class<?> b) {
		if (a == null) {
			throw new IllegalArgumentException("a must not be null");
		}
		if (b == null) {
			throw new IllegalArgumentException("b must not be null");
		}
		if (a == Byte.class || a == byte.class) {
			return b == Byte.class || b == byte.class;
		}
		if (a == Short.class || a == short.class) {
			return b == Short.class || b == short.class;
		}
		if (a == Integer.class || a == int.class) {
			return b == Integer.class || b == int.class;
		}
		if (a == Long.class || a == long.class) {
			return b == Long.class || b == long.class;
		}
		if (a == Double.class || a == double.class) {
			return b == Double.class || b == double.class;
		}
		if (a == Float.class || a == float.class) {
			return b == Float.class || b == float.class;
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类!");
	}

	/**
	 * 基本数据类型适配<br>
	 * value为null时,直接返回null<br>
	 * 涉及类型:byte, short, int, long, double, float以及对应装箱类,还有void
	 * @author Frodez
	 */
	public static <T> T cast(Object value, Class<T> parameterClass) {
		if (value == null) {
			return null;
		}
		Class<?> valueClass = value.getClass();
		if (valueClass == Byte.class || valueClass == byte.class) {
			return castByteValue(parameterClass, (Byte) value);
		} else if (valueClass == Short.class || valueClass == short.class) {
			return castShortValue(parameterClass, (Short) value);
		} else if (valueClass == Integer.class || valueClass == int.class) {
			return castIntValue(parameterClass, (Integer) value);
		} else if (valueClass == Long.class || valueClass == long.class) {
			return castLongValue(parameterClass, (Long) value);
		} else if (valueClass == Double.class || valueClass == double.class) {
			return castDoubleValue(parameterClass, (Double) value);
		} else if (valueClass == Float.class || valueClass == float.class) {
			return castFloatValue(parameterClass, (Float) value);
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类,以及void类型!");
	}

	@SuppressWarnings("unchecked")
	private static <T> T castByteValue(Class<T> parameterClass, Byte value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) value;
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) Short.valueOf(value.shortValue());
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) Integer.valueOf(value.intValue());
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) Long.valueOf(value.longValue());
		}
		if (parameterClass == Float.class || parameterClass == float.class) {
			return (T) Float.valueOf(value.floatValue());
		}
		if (parameterClass == Double.class || parameterClass == double.class) {
			return (T) Double.valueOf(value.doubleValue());
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类,以及void类型!");
	}

	@SuppressWarnings("unchecked")
	private static <T> T castShortValue(Class<T> parameterClass, Short value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) value;
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) Integer.valueOf(value.intValue());
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) Long.valueOf(value.longValue());
		}
		if (parameterClass == Float.class || parameterClass == float.class) {
			return (T) Float.valueOf(value.floatValue());
		}
		if (parameterClass == Double.class || parameterClass == double.class) {
			return (T) Double.valueOf(value.doubleValue());
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类,以及void类型!");
	}

	@SuppressWarnings("unchecked")
	private static <T> T castIntValue(Class<T> parameterClass, Integer value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) Short.valueOf(value.shortValue());
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) value;
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) Long.valueOf(value.longValue());
		}
		if (parameterClass == Float.class || parameterClass == float.class) {
			return (T) Float.valueOf(value.floatValue());
		}
		if (parameterClass == Double.class || parameterClass == double.class) {
			return (T) Double.valueOf(value.doubleValue());
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类,以及void类型!");
	}

	@SuppressWarnings("unchecked")
	private static <T> T castLongValue(Class<T> parameterClass, Long value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) Short.valueOf(value.shortValue());
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) Integer.valueOf(value.intValue());
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) value;
		}
		if (parameterClass == Float.class || parameterClass == float.class) {
			return (T) Float.valueOf(value.floatValue());
		}
		if (parameterClass == Double.class || parameterClass == double.class) {
			return (T) Double.valueOf(value.doubleValue());
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类,以及void类型!");
	}

	@SuppressWarnings("unchecked")
	private static <T> T castDoubleValue(Class<T> parameterClass, Double value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) Short.valueOf(value.shortValue());
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) Integer.valueOf(value.intValue());
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) Long.valueOf(value.longValue());
		}
		if (parameterClass == Float.class || parameterClass == float.class) {
			return (T) Float.valueOf(value.floatValue());
		}
		if (parameterClass == Double.class || parameterClass == double.class) {
			return (T) value;
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类,以及void类型!");
	}

	@SuppressWarnings("unchecked")
	private static <T> T castFloatValue(Class<T> parameterClass, Float value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) Short.valueOf(value.shortValue());
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) Integer.valueOf(value.intValue());
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) Long.valueOf(value.longValue());
		}
		if (parameterClass == Float.class || parameterClass == float.class) {
			return (T) value;
		}
		if (parameterClass == Double.class || parameterClass == double.class) {
			return (T) Double.valueOf(value.doubleValue());
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long, double, float以及对应装箱类,以及void类型!");
	}

}
