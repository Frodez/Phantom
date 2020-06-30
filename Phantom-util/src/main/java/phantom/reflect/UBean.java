package phantom.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import phantom.common.UPrimitive;
import phantom.common.UString;

/**
 * Java Bean工具类<br>
 * 建议不要在项目初始化阶段使用,而用于日常业务或者已经初始化完毕后。<br>
 * @author Frodez
 */
@Slf4j
@UtilityClass
public class UBean {

	private static final Map<MultiTypeKey, BeanCopier> COPIER_CACHE = new ConcurrentHashMap<>();

	private static final Map<Class<?>, List<FastMethod>> SETTER_CACHE = new ConcurrentHashMap<>();

	private static final Map<Class<?>, List<FastMethod>> NOT_NULL_FIELD_SETTER_CACHE = new ConcurrentHashMap<>();

	private static BeanCopier getCopier(Object source, Object target) {
		Class<?> s = source.getClass();
		Class<?> t = target.getClass();
		return COPIER_CACHE.computeIfAbsent(new MultiTypeKey(s, t), i -> BeanCopier.create(s, t, false));
	}

	/**
	 * copy对象属性<br>
	 * 建议对数据库insert时使用本方法，update时使用cover方法。<br>
	 * @see UBean.util.reflect.BeanUtil#cover(Object, Object)
	 * @author Frodez
	 */
	public static void copy(Object source, Object target) {
		getCopier(source, target).copy(source, target, null);
	}

	/**
	 * copy对象属性<br>
	 * 建议对数据库insert时使用本方法，update时使用cover方法。<br>
	 * @see UBean.util.reflect.BeanUtil#cover(Object, Object)
	 * @author Frodez
	 */
	public static <T> T copy(Object source, Supplier<T> supplier) {
		T target = supplier.get();
		getCopier(source, target).copy(source, target, null);
		return target;
	}

	/**
	 * 创造一个不具有初始值的,copy自原对象属性的bean<br>
	 * 建议对数据库update时使用本方法，insert时使用copy方法。<br>
	 * 在使用本方法和使用BeanUtil.cover(Object, Object)方法的意义相同时,建议使用本方法,速度更快。<br>
	 * @see UBean.util.reflect.BeanUtil#cover(Object, Object)
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T initialize(Object source, Class<T> target) {
		T bean = clearInstance(target);
		getCopier(source, bean).copy(source, bean, null);
		return bean;
	}

	/**
	 * 清空目标bean的所有属性,然后copy原对象属性.<br>
	 * 建议对数据库update时使用本方法，insert时使用copy方法。<br>
	 * 在使用本方法和使用BeanUtil.initialize(Object,
	 * Class)方法的意义相同时,建议使用initialize方法,速度更快。<br>
	 * 注意:请不要这样使用本方法:<br>
	 *
	 * <pre>
	 * Bean one = new Bean();
	 * Bean two = new Bean();
	 * BeanUtil.cover(one, two);
	 * </pre>
	 *
	 * 因为本方法先清空属性然后赋值,所以如果这样做,未清空属性的bean会将清空部分覆盖.<br>
	 * 请务必考虑清楚,原对象不为null的属性有哪些.<br>
	 * 否则这样使用等于无用功.<br>
	 * @see UBean.util.reflect.BeanUtil#copy(Object, Object)
	 * @see UBean.util.reflect.BeanUtil#initialize(Object, Class)
	 * @author Frodez
	 */
	@SneakyThrows
	public static void cover(Object source, Object target) {
		clear(target);
		getCopier(source, target).copy(source, target, null);
	}

	/**
	 * bean转map
	 * @author Frodez
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> map(Object bean) {
		if (bean == null) {
			throw new IllegalArgumentException();
		}
		return new HashMap<>(BeanMap.create(bean));
	}

	/**
	 * map转bean
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T as(Map<String, Object> map, Supplier<T> supplier) {
		if (map == null) {
			throw new IllegalArgumentException();
		}
		T bean = supplier.get();
		try {
			BeanMap.create(bean).putAll(map);
		} catch (Exception e) {
			log.error("map转bean出现异常,采用安全方式继续...异常消息:{}", e.getMessage());
			// 如果出错则采用安全但较慢的方式
			safeAs(map, bean);
		}
		return bean;
	}

	/**
	 * map转bean
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T as(Map<String, Object> map, Class<T> klass) {
		if (map == null) {
			throw new IllegalArgumentException();
		}
		T bean = UReflect.instance(klass);
		try {
			BeanMap.create(bean).putAll(map);
		} catch (Exception e) {
			log.error("map转bean出现异常,采用安全方式继续...异常消息:{}", e.getMessage());
			// 如果出错则采用安全但较慢的方式
			safeAs(map, bean);
		}
		return bean;
	}

	@SuppressWarnings("unchecked")
	@SneakyThrows
	private static <T> void safeAs(Map<String, Object> map, T bean) {
		List<FastMethod> methods = setters(bean.getClass());
		Object[] param = new Object[1];
		for (FastMethod fastMethod : methods) {
			String field = UString.lowerFirst(fastMethod.getName().substring(3));
			Object value = map.get(field);
			if (value == null) {
				param[0] = null;
			} else if (UPrimitive.isBaseType(value.getClass())) {
				param[0] = UPrimitive.cast(value, fastMethod.getParameterTypes()[0]);
			} else {
				param[0] = value;
			}
			fastMethod.invoke(bean, param);
		}
	}

	/**
	 * 清空bean的默认值<br>
	 * 如果只需要一个全新的无默认值的对象,建议使用BeanUtil.clearInstance方法,<br>
	 * 原因是本方法会对所有字段执行其setter方法(无法确保只有存在默认值的字段拥有值),开销更大。<br>
	 * @see UBean#clearInstance(Class)
	 * @author Frodez
	 */
	@SneakyThrows
	public static void clear(Object bean) {
		if (bean == null) {
			throw new IllegalArgumentException();
		}
		List<FastMethod> methods = setters(bean.getClass());
		int length = methods.size();
		for (int i = 0; i < length; i++) {
			methods.get(i).invoke(bean, UReflect.EMPTY_ARRAY);
		}
	}

	private static boolean isSetter(Method method) {
		return method.getName().startsWith("set") && method.getReturnType() == void.class
				&& method.getParameterCount() == 1
				&& Modifier.PUBLIC == method.getModifiers();
	}

	private static boolean isPrivateAndNotNullField(Field field, Object bean)
			throws IllegalArgumentException, IllegalAccessException {
		return Modifier.PRIVATE == field.getModifiers() && field.trySetAccessible() && field.get(bean) != null;
	}

	/**
	 * 获取setter对应的field
	 * @author Frodez
	 */
	public static List<Field> getSetterFields(Class<?> klass) {
		List<Field> fields = new ArrayList<>();
		List<Method> setters = new ArrayList<>();
		for (Method method : klass.getMethods()) {
			if (isSetter(method)) {
				setters.add(method);
			}
		}
		for (Field field : klass.getDeclaredFields()) {
			if (Modifier.PRIVATE == field.getModifiers()) {
				for (Method method : setters) {
					if (method.getName().endsWith(UString.upperFirst(field.getName()))) {
						fields.add(field);
						break;
					}
				}
			}
		}
		return Collections.unmodifiableList(fields);
	}

	/**
	 * 获取所有setters
	 * @author Frodez
	 */
	public static List<FastMethod> getSetters(Class<?> klass) {
		return Collections.unmodifiableList(setters(klass));
	}

	private static List<FastMethod> setters(Class<?> klass) {
		List<FastMethod> methods = SETTER_CACHE.get(klass);
		if (methods == null) {
			methods = new ArrayList<>();
			FastClass fastClass = FastClass.create(klass);
			for (Method method : klass.getMethods()) {
				if (isSetter(method)) {
					methods.add(fastClass.getMethod(method));
				}
			}
			SETTER_CACHE.put(klass, methods);
		}
		return methods;
	}

	/**
	 * 获取所有非空的setters
	 * @author Frodez
	 */
	@SneakyThrows
	public static List<FastMethod> getDefaultNotNullSetters(Class<?> klass) {
		return Collections.unmodifiableList(defaultNotNullSetters(UReflect.instance(klass)));
	}

	private static List<FastMethod> defaultNotNullSetters(Object bean)
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> klass = bean.getClass();
		List<FastMethod> methods = NOT_NULL_FIELD_SETTER_CACHE.get(klass);
		if (methods == null) {
			methods = new ArrayList<>();
			FastClass fastClass = FastClass.create(klass);
			List<Method> setters = new ArrayList<>();
			for (Method method : klass.getMethods()) {
				if (isSetter(method)) {
					setters.add(method);
				}
			}
			for (Field field : klass.getDeclaredFields()) {
				if (isPrivateAndNotNullField(field, bean)) {
					for (Method method : setters) {
						if (method.getName().endsWith(UString.upperFirst(field.getName()))) {
							methods.add(fastClass.getMethod(method));
							break;
						}
					}
				}
			}
			NOT_NULL_FIELD_SETTER_CACHE.put(klass, methods);
		}
		return methods;
	}

	/**
	 * 获取无默认值的bean。<br>
	 * 推荐使用本方法,比新建一个对象然后使用BeanUtil.clear更快,<br>
	 * 原因是本方法只会执行存在默认值的字段的setter方法,方法执行的开销减小。<br>
	 * @see UBean#clear(Object)
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T clearInstance(Class<T> klass) {
		T bean = UReflect.instance(klass);
		List<FastMethod> methods = defaultNotNullSetters(bean);
		int length = methods.size();
		for (int i = 0; i < length; i++) {
			methods.get(i).invoke(bean, UReflect.EMPTY_ARRAY);
		}
		return bean;
	}

}
