package phantom.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

/**
 * stream api 辅助工具类
 * @author Frodez
 */
@UtilityClass
public class UStream {

	/**
	 * 对stream进行分页查询
	 * @param pageNum 页码数。为了和mysql分页查询保持一致,小于等于1时,忽略该参数。
	 * @param pageSize 单页容量
	 * @author Frodez
	 */
	public static <T> Stream<T> page(Collection<T> collection, int pageNum, int pageSize) {
		if (pageNum < 0) {
			throw new IllegalArgumentException("pageNum can't be negetive");
		}
		if (pageSize <= 0) {
			throw new IllegalArgumentException("pageSize must be positive");
		}
		return pageNum > 1 ? collection.stream().skip((pageNum - 1) * pageSize).limit(pageSize) : collection.stream().limit(pageSize);
	}

	/**
	 * 对stream进行分页查询
	 * @param limit mysql风格的limit
	 * @param offset mysql风格的offset
	 * @author Frodez
	 */
	public static <T> Stream<T> rowBounds(Collection<T> collection, int limit, int offset) {
		if (limit < 0) {
			throw new IllegalArgumentException("limit can't be negetive");
		}
		if (offset <= 0) {
			throw new IllegalArgumentException("offset must be positive");
		}
		return limit > 0 ? collection.stream().skip(offset).limit(limit) : collection.stream().limit(limit);
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 */
	public static <T, R> List<R> list(Collection<T> collection, Function<T, R> function) {
		return collection.stream().map(function).collect(Collectors.toList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 */
	public static <T, R> List<R> list(T[] collection, Function<T, R> function) {
		return Stream.of(collection).map(function).collect(Collectors.toList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 */
	public static <T> List<T> filterList(Collection<T> collection, Predicate<T> predicate) {
		return collection.stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 */
	public static <T> List<T> filterList(T[] collection, Predicate<T> predicate) {
		return Stream.of(collection).filter(predicate).collect(Collectors.toList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 */
	public static <T, R> List<R> unmodifiableList(Collection<T> collection, Function<T, R> function) {
		return collection.stream().map(function).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 */
	public static <T, R> List<R> unmodifiableList(T[] collection, Function<T, R> function) {
		return Stream.of(collection).map(function).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 */
	public static <T> List<T> filterUnmodifiableList(Collection<T> collection, Predicate<T> predicate) {
		return collection.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 */
	public static <T> List<T> filterUnmodifiableList(T[] collection, Predicate<T> predicate) {
		return Stream.of(collection).filter(predicate).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 */
	public static <T, R> Set<R> set(Collection<T> collection, Function<T, R> function) {
		return collection.stream().map(function).collect(Collectors.toSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 */
	public static <T, R> Set<R> set(T[] collection, Function<T, R> function) {
		return Stream.of(collection).map(function).collect(Collectors.toSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 */
	public static <T> Set<T> filterSet(Collection<T> collection, Predicate<T> predicate) {
		return collection.stream().filter(predicate).collect(Collectors.toSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 */
	public static <T> Set<T> filterSet(T[] collection, Predicate<T> predicate) {
		return Stream.of(collection).filter(predicate).collect(Collectors.toSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 */
	public static <T, R> Set<R> unmodifiableSet(Collection<T> collection, Function<T, R> function) {
		return collection.stream().map(function).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 */
	public static <T, R> Set<R> unmodifiableSet(T[] collection, Function<T, R> function) {
		return Stream.of(collection).map(function).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 */
	public static <T> Set<T> filterUnmodifiableSet(Collection<T> collection, Predicate<T> predicate) {
		return collection.stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 */
	public static <T> Set<T> filterUnmodifiableSet(T[] collection, Predicate<T> predicate) {
		return Stream.of(collection).filter(predicate).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * Map stream collector
	 * @param T stream的元素类型
	 * @param K Map key的类型
	 * @param V Map value的类型
	 * @author Frodez
	 */
	public static class MapBuilder<T, K, V> {

		Function<T, K> keyMapper;

		Function<T, V> valueMapper;

		BinaryOperator<V> mergeFunction;

		private MapBuilder() {
		}

		private void checkState() {
			if (keyMapper == null) {
				throw new IllegalArgumentException("keyMapper must not be null");
			}
			if (valueMapper == null) {
				throw new IllegalArgumentException("valueMapper must not be null");
			}
		}

		public static <T, K, V> MapBuilder<T, K, V> instance() {
			return new MapBuilder<>();
		}

		/**
		 * 配置keyMapper
		 * @author Frodez
		 */
		public synchronized MapBuilder<T, K, V> key(Function<T, K> keyMapper) {
			if (this.keyMapper != null) {
				throw new IllegalStateException();
			}
			this.keyMapper = keyMapper;
			return this;
		}

		/**
		 * 配置valueMapper
		 * @author Frodez
		 */
		public synchronized MapBuilder<T, K, V> value(Function<T, V> valueMapper) {
			if (this.valueMapper != null) {
				throw new IllegalStateException();
			}
			this.valueMapper = valueMapper;
			return this;
		}

		/**
		 * 配置mergeFunction(可选)
		 * @author Frodez
		 */
		public synchronized MapBuilder<T, K, V> merge(BinaryOperator<V> mergeFunction) {
			if (this.mergeFunction != null) {
				throw new IllegalStateException();
			}
			this.mergeFunction = mergeFunction;
			return this;
		}

		@SuppressWarnings("unchecked")
		private BinaryOperator<V> mergeFunction() {
			if (mergeFunction == null) {
				return (m1, m2) -> {
					for (Map.Entry<K, V> e : ((Map<K, V>) m2).entrySet()) {
						K k = e.getKey();
						V v = e.getValue();
						V u = ((Map<K, V>) m1).putIfAbsent(k, v);
						if (u != null) {
							throw new IllegalStateException(String.format("Duplicate key %s (attempted merging values %s and %s)", k, u, v));
						}
					}
					return m1;
				};
			}
			return mergeFunction;
		}

		/**
		 * 返回collector
		 * @author Frodez
		 */
		public Collector<T, ?, Map<K, V>> map(Supplier<Map<K, V>> supplier) {
			checkState();
			return Collectors.toMap(keyMapper, valueMapper, mergeFunction(), supplier);
		}

		/**
		 * 返回map
		 * @author Frodez
		 */
		public Map<K, V> map(Stream<T> stream, Supplier<Map<K, V>> supplier) {
			return stream.collect(map(supplier));
		}

		/**
		 * 返回collector
		 * @author Frodez
		 */
		public Collector<T, ?, Map<K, V>> hashMap() {
			checkState();
			return Collectors.toMap(keyMapper, valueMapper, mergeFunction(), HashMap::new);
		}

		/**
		 * 返回hashMap
		 * @author Frodez
		 */
		public Map<K, V> hashMap(Stream<T> stream) {
			return stream.collect(hashMap());
		}

		/**
		 * 返回collector
		 * @author Frodez
		 */
		public Collector<T, ?, ConcurrentHashMap<K, V>> concurrentHashMap() {
			checkState();
			return Collectors.toConcurrentMap(keyMapper, valueMapper, mergeFunction(), ConcurrentHashMap::new);
		}

		/**
		 * 返回concurrentHashMap
		 * @author Frodez
		 */
		public ConcurrentHashMap<K, V> concurrentHashMap(Stream<T> stream) {
			return stream.collect(concurrentHashMap());
		}

		/**
		 * 返回collector
		 * @author Frodez
		 */
		public Collector<T, ?, Map<K, V>> unmodifiableMap() {
			checkState();
			return Collectors.toUnmodifiableMap(keyMapper, valueMapper, mergeFunction());
		}

		/**
		 * 返回unmodifiableMap
		 * @author Frodez
		 */
		public Map<K, V> unmodifiableMap(Stream<T> stream) {
			return stream.collect(unmodifiableMap());
		}

	}

}
