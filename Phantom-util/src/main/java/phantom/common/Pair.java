package phantom.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Pair
 * @author Frodez
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Pair<F, S> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * first
	 */
	private F first;

	/**
	 * second
	 */
	private S second;

	public Pair(Map.Entry<F, S> entry) {
		this.first = entry.getKey();
		this.second = entry.getValue();
	}

	public static <K, V> ArrayList<Pair<K, V>> transfer(Map<K, V> map) {
		ArrayList<Pair<K, V>> collection = new ArrayList<>();
		for (Entry<K, V> entry : map.entrySet()) {
			collection.add(new Pair<>(entry));
		}
		return collection;
	}

	public static <C extends Collection<Pair<K, V>>, K, V> C transfer(Map<K, V> map, Supplier<C> supplier) {
		C collection = supplier.get();
		for (Entry<K, V> entry : map.entrySet()) {
			collection.add(new Pair<>(entry));
		}
		return collection;
	}

	public static <C extends Collection<P>, P extends Pair<K, V>, K, V> C transfer(Map<K, V> map, Supplier<C> collectionSupplier, BiFunction<K, V,
		P> pairSupplier) {
		C collection = collectionSupplier.get();
		for (Entry<K, V> entry : map.entrySet()) {
			collection.add(pairSupplier.apply(entry.getKey(), entry.getValue()));
		}
		return collection;
	}

}
