package phantom.tool.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;
import phantom.common.UEmpty;
import phantom.reflect.MultiTypeKey;

/**
 * json工具类<br>
 * 本工具类使用的ObjectMapper是jackson的springboot-starter所自动配置得到的,不会使得项目中引入两个不同的ObjectMapper。<br>
 * 本工具类对html字符的转义做了专门处理。<br>
 * 本工具类大量采用缓存,可较大程度上提高速度。<br>
 * @author Frodez
 */
public class JSON {

	private static ObjectMapper OBJECT_MAPPER;

	@SuppressWarnings("rawtypes")
	private static Class<HashMap> DEFAULT_MAP_CLASS = HashMap.class;

	@SuppressWarnings("rawtypes")
	private static Class<ArrayList> DEFAULT_LIST_CLASS = ArrayList.class;

	@SuppressWarnings("rawtypes")
	private static Class<HashSet> DEFAULT_SET_CLASS = HashSet.class;

	private static ObjectReader DEFAULT_MAP_READER;

	private static Map<Class<?>, ObjectReader> singleClassReaderCache = new ConcurrentHashMap<>();

	private static Map<Type, ObjectReader> singleTypeReaderCache = new ConcurrentHashMap<>();

	private static Map<MultiTypeKey, ObjectReader> multiClassReaderCache = new ConcurrentHashMap<>();

	private static Map<Class<?>, ObjectWriter> writerCache = new ConcurrentHashMap<>();

	static {
		OBJECT_MAPPER = new ObjectMapper();
		OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		DEFAULT_MAP_READER = OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructParametricType(DEFAULT_MAP_CLASS, String.class,
			Object.class));
	}

	/**
	 * 获取jackson对象
	 * @author Frodez
	 */
	public static ObjectMapper mapper() {
		return OBJECT_MAPPER;
	}

	/**
	 * 将对象转换成json字符串
	 * @author Frodez
	 */
	@SneakyThrows
	public static String string(Object object) {
		Class<?> klass = object.getClass();
		return writerCache.computeIfAbsent(klass, (o) -> OBJECT_MAPPER.writerFor(klass)).writeValueAsString(object);
	}

	/**
	 * 获取对象对应的ObjectWriter
	 * @author Frodez
	 */
	@SneakyThrows
	public static ObjectWriter writer(Object object) {
		Class<?> klass = object.getClass();
		return writerCache.computeIfAbsent(klass, (o) -> OBJECT_MAPPER.writerFor(klass));
	}

	/**
	 * 获取Reader
	 * @author Frodez
	 */
	@SneakyThrows
	public static ObjectReader reader(Class<?> parametrized, Class<?>... genericClasses) {
		if (UEmpty.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized)));
		} else {
			return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(parametrized, genericClasses), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
				.getTypeFactory().constructParametricType(parametrized, genericClasses)));
		}
	}

	/**
	 * 将InputStream的数据转换成Map
	 * @author Frodez
	 */
	@SneakyThrows
	public static Map<String, Object> map(InputStream stream) {
		return DEFAULT_MAP_READER.readValue(stream);
	}

	/**
	 * 将json字符串转换成Map
	 * @author Frodez
	 */
	@SneakyThrows
	public static Map<String, Object> map(String json) {
		return DEFAULT_MAP_READER.readValue(json);
	}

	/**
	 * 将InputStream的数据转换成Map
	 * @author Frodez
	 */
	@SneakyThrows
	public static <K, V> Map<K, V> map(InputStream stream, Class<K> k, Class<V> v) {
		return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(DEFAULT_MAP_CLASS, k, v), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
			.getTypeFactory().constructParametricType(DEFAULT_MAP_CLASS, k, v))).readValue(stream);
	}

	/**
	 * 将json字符串转换成Map
	 * @author Frodez
	 */
	@SneakyThrows
	public static <K, V> Map<K, V> map(String json, Class<K> k, Class<V> v) {
		return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(DEFAULT_MAP_CLASS, k, v), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
			.getTypeFactory().constructParametricType(DEFAULT_MAP_CLASS, k, v))).readValue(json);
	}

	/**
	 * 将InputStream的数据转换成List
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> List<T> list(InputStream stream, Class<T> klass) {
		return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(DEFAULT_LIST_CLASS, klass), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
			.getTypeFactory().constructParametricType(DEFAULT_LIST_CLASS, klass))).readValue(stream);
	}

	/**
	 * 将json字符串转换成List
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> List<T> list(String json, Class<T> klass) {
		return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(DEFAULT_LIST_CLASS, klass), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
			.getTypeFactory().constructParametricType(DEFAULT_LIST_CLASS, klass))).readValue(json);
	}

	/**
	 * 将InputStream的数据转换成Set
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> Set<T> set(InputStream stream, Class<T> klass) {
		return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(DEFAULT_SET_CLASS, klass), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
			.getTypeFactory().constructParametricType(DEFAULT_SET_CLASS, klass))).readValue(stream);
	}

	/**
	 * 将json字符串转换成Set
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> Set<T> set(String json, Class<T> klass) {
		return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(DEFAULT_SET_CLASS, klass), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
			.getTypeFactory().constructParametricType(DEFAULT_SET_CLASS, klass))).readValue(json);
	}

	/**
	 * 将InputStream的数据转换成对象
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T as(InputStream stream, Class<T> klass) {
		return singleClassReaderCache.computeIfAbsent(klass, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(klass)))
			.readValue(stream);
	}

	/**
	 * 将json字符串转换成对象
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T as(String json, Class<T> klass) {
		return singleClassReaderCache.computeIfAbsent(klass, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(klass)))
			.readValue(json);
	}

	/**
	 * 将InputStream的数据转换成对象
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T as(InputStream stream, Type type) {
		return singleTypeReaderCache.computeIfAbsent(type, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(type)))
			.readValue(stream);
	}

	/**
	 * 将json字符串转换成对象
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T as(String json, Type type) {
		return singleTypeReaderCache.computeIfAbsent(type, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(type)))
			.readValue(json);
	}

	/**
	 * 将InputStream的数据转换成任意类型
	 * @param parametrized 原型
	 * @param genericClasses 泛型,按从外到内的顺序从前往后排列
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T as(InputStream stream, Class<T> parametrized, Class<?>... genericClasses) {
		if (UEmpty.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized))).readValue(stream);
		} else {
			return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(parametrized, genericClasses), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
				.getTypeFactory().constructParametricType(parametrized, genericClasses))).readValue(stream);
		}
	}

	/**
	 * 将json字符串转换成任意类型
	 * @param parametrized 原型
	 * @param genericClasses 泛型,按从外到内的顺序从前往后排列
	 * @author Frodez
	 */
	@SneakyThrows
	public static <T> T as(String json, Class<T> parametrized, Class<?>... genericClasses) {
		if (UEmpty.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized))).readValue(json);
		} else {
			return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(parametrized, genericClasses), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
				.getTypeFactory().constructParametricType(parametrized, genericClasses))).readValue(json);
		}
	}

	/**
	 * 将json字符串转换成任意类型(类型已擦除)
	 * @param parametrized 原型
	 * @param genericClasses 泛型,按从外到内的顺序从前往后排列
	 * @author Frodez
	 */
	@SneakyThrows
	public static Object object(String json, Class<?> parametrized, Class<?>... genericClasses) {
		if (UEmpty.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized))).readValue(json);
		} else {
			return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(parametrized, genericClasses), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
				.getTypeFactory().constructParametricType(parametrized, genericClasses))).readValue(json);
		}
	}

	/**
	 * 将InputStream的数据转换成任意类型(类型已擦除)
	 * @param parametrized 原型
	 * @param genericClasses 泛型,按从外到内的顺序从前往后排列
	 * @author Frodez
	 */
	@SneakyThrows
	public static Object object(InputStream stream, Class<?> parametrized, Class<?>... genericClasses) {
		if (UEmpty.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized))).readValue(stream);
		} else {
			return multiClassReaderCache.computeIfAbsent(new MultiTypeKey(parametrized, genericClasses), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER
				.getTypeFactory().constructParametricType(parametrized, genericClasses))).readValue(stream);
		}
	}

}
