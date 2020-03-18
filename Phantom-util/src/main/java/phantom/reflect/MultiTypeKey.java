package phantom.reflect;

import java.lang.reflect.Type;

/**
 * 对多个type的封装,可以公开使用作为key
 * @author Frodez
 */
public final class MultiTypeKey {

	private int[] hashes;

	private int hash = 0;

	public MultiTypeKey(Type... types) {
		this.hashes = new int[types.length];
		for (int i = 0; i < types.length; i++) {
			Type type = types[i];
			if (type != null) {
				int hash = type.hashCode();
				hashes[i] = hash;
				this.hash = this.hash + hash << i;
			}
		}
	}

	public MultiTypeKey(Class<?> parametrized, Type... generics) {
		this.hashes = new int[generics.length + 1];
		this.hashes[0] = parametrized.hashCode();
		for (int i = 0, index = 1; i < generics.length; i++, index++) {
			Type type = generics[i];
			if (type != null) {
				int hash = type.hashCode();
				hashes[index] = hash;
				this.hash = this.hash + hash << i;
			}
		}
	}

	@Override
	public int hashCode() {
		return this.hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || MultiTypeKey.class != obj.getClass()) {
			return false;
		}
		MultiTypeKey another = (MultiTypeKey) obj;
		if (this.hashes.length != another.hashes.length) {
			return false;
		}
		for (int i = 0; i < this.hashes.length; i++) {
			if (this.hashes[i] != another.hashes[i]) {
				return false;
			}
		}
		return true;
	}

}
