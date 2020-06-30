package phantom.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import phantom.common.UString;

/**
 * 反射工具类<br>
 * @author Frodez
 */
@UtilityClass
public class UReflect {

	public static final Lookup LOOKUP = MethodHandles.lookup();

	public static final Object[] EMPTY_ARRAY = new Object[] { null };

	private static final Map<Class<?>, Table> CGLIB_CACHE = new ConcurrentHashMap<>();

	private static final Map<String, MethodHandle> GETTER_CACHE = new ConcurrentHashMap<>();

	private static final Map<String, MethodHandle> SETTER_CACHE = new ConcurrentHashMap<>();

	/**
	 * 对象实例化
	 * @author Frodez
	 */
	@SneakyThrows
	@SuppressWarnings("unchecked")
	public static <T> T instance(Class<T> klass) {
		return (T) fastClass(klass).newInstance();
	}

	/**
	 * 获取FastClass,类型会被缓存
	 * @author Frodez
	 */
	public static FastClass fastClass(Class<?> klass) {
		return fastClass(klass, true);
	}

	/**
	 * 获取FastClass,并初始化所有FastMethod。类型会被缓存。
	 * @author Frodez
	 */
	public static FastClass fastClass(Class<?> klass, boolean initialized) {
		Table table = CGLIB_CACHE.get(klass);
		if (table == null) {
			table = new Table(klass);
			if (initialized) {
				table.init();
			}
			CGLIB_CACHE.put(klass, table);
			return table.fastClass;
		}
		return table.fastClass;
	}

	/**
	 * 获取FastMethod,方法会被缓存,无法获取和调用私有方法。
	 * @author Frodez
	 */
	@SneakyThrows
	public static FastMethod fastMethod(Class<?> klass, String method, Class<?>... params) {
		if (method == null) {
			throw new IllegalArgumentException();
		}
		Table table = CGLIB_CACHE.get(klass);
		if (table == null) {
			table = new Table(klass);
			FastMethod fastMethod = table.method(method, params);
			CGLIB_CACHE.put(klass, table);
			return fastMethod;
		}
		return table.method(method, params);
	}

	private static class Table {

		FastClass fastClass;

		FastMethod[] methods;

		public Table(Class<?> klass) {
			fastClass = FastClass.create(klass);
			methods = new FastMethod[fastClass.getMaxIndex() + 1];
		}

		public void init() {
			for (Method method : fastClass.getJavaClass().getMethods()) {
				int index = fastClass.getIndex(method.getName(), method.getParameterTypes());
				if (index < 0 || index >= methods.length) {
					continue;
				}
				methods[index] = fastClass.getMethod(method);
			}
		}

		@SneakyThrows
		FastMethod method(String name, Class<?>... params) {
			int index = fastClass.getIndex(name, params);
			if (index < 0) {
				throw new NoSuchMethodException();
			}
			FastMethod method = methods[index];
			if (method == null) {
				method = fastClass.getMethod(name, params);
				methods[index] = method;
			}
			return method;
		}

	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 */
	public static String fullName(Method method) {
		return UString.concat(method.getDeclaringClass().getCanonicalName(), ".", method.getName());
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 */
	public static String shortName(Method method) {
		return UString.concat(method.getDeclaringClass().getSimpleName(), ".", method.getName());
	}

	/**
	 * 获取字段全限定名
	 * @author Frodez
	 */
	public static String fullName(Field field) {
		return UString.concat(field.getDeclaringClass().getCanonicalName(), ".", field.getName());
	}

	/**
	 * 获取字段全限定名
	 * @author Frodez
	 */
	public static String shortName(Field field) {
		return UString.concat(field.getDeclaringClass().getSimpleName(), ".", field.getName());
	}

	/**
	 * 设置指定字段<br>
	 * target为null时,设置静态字段
	 * @author Frodez
	 */
	@SuppressWarnings("deprecation")
	public static void set(Class<?> klass, String fieldName, Object target, Object value) throws Throwable {
		String identifier = UString.concat(klass.getCanonicalName(), ".", fieldName);
		MethodHandle handle = SETTER_CACHE.get(identifier);
		if (handle == null) {
			// 首次使用该字段时,一定会走入本判断分支中。只要不将字段可见性重新设置为false，就不会出现错误
			Field field = klass.getDeclaredField(fieldName);
			if (!field.isAccessible()) {
				// 暂时使用isAccessible api,因为可以减少判断次数提高性能
				field.trySetAccessible();
			}
			handle = LOOKUP.unreflectSetter(field);
			SETTER_CACHE.put(identifier, handle);
		}
		if (target == null) {
			handle.invoke(value);
		} else {
			handle.invoke(target, value);
		}
	}

	/**
	 * 设置指定字段<br>
	 * target为null时,设置静态字段
	 * @author Frodez
	 */
	@SuppressWarnings("deprecation")
	public static void set(Field field, Object target, Object value) throws Throwable {
		String identifier = UString.concat(field.getDeclaringClass().getCanonicalName(), ".", field.getName());
		MethodHandle handle = SETTER_CACHE.get(identifier);
		if (handle == null) {
			// 首次使用该字段时,一定会走入本判断分支中。只要不将字段可见性重新设置为false，就不会出现错误
			if (!field.isAccessible()) {
				// 暂时使用isAccessible api,因为可以减少判断次数提高性能
				field.trySetAccessible();
			}
			handle = LOOKUP.unreflectGetter(field);
			GETTER_CACHE.put(identifier, handle);
		}
		if (target == null) {
			handle.invoke(value);
		} else {
			handle.invoke(target, value);
		}
	}

	/**
	 * 获取指定字段<br>
	 * target为null时,获取静态字段
	 */
	@SuppressWarnings("deprecation")
	public static Object get(Class<?> klass, String fieldName, Object target) throws Throwable {
		String identifier = UString.concat(klass.getCanonicalName(), ".", fieldName);
		MethodHandle handle = GETTER_CACHE.get(identifier);
		if (handle == null) {
			// 首次使用该字段时,一定会走入本判断分支中。只要不将字段可见性重新设置为false，就不会出现错误
			Field field = klass.getDeclaredField(fieldName);
			if (!field.isAccessible()) {
				// 暂时使用isAccessible api,因为可以减少判断次数提高性能
				field.trySetAccessible();
			}
			handle = LOOKUP.unreflectGetter(field);
			GETTER_CACHE.put(identifier, handle);
		}
		return target == null ? handle.invoke() : handle.invoke(target);
	}

	/**
	 * 获取指定字段<br>
	 * target为null时,获取静态字段
	 * @author Frodez
	 */
	@SuppressWarnings("deprecation")
	public static Object get(Field field, Object target) throws Throwable {
		String identifier = UString.concat(field.getDeclaringClass().getCanonicalName(), ".", field.getName());
		MethodHandle handle = GETTER_CACHE.get(identifier);
		if (handle == null) {
			// 首次使用该字段时,一定会走入本判断分支中。只要不将字段可见性重新设置为false，就不会出现错误
			if (!field.isAccessible()) {
				// 暂时使用isAccessible api,因为可以减少判断次数提高性能
				field.trySetAccessible();
			}
			handle = LOOKUP.unreflectGetter(field);
			GETTER_CACHE.put(identifier, handle);
		}
		return target == null ? handle.invoke() : handle.invoke(target);
	}

}
