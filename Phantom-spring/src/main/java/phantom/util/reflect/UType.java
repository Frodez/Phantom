package phantom.util.reflect;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.types.ResolvedInterfaceType;
import com.fasterxml.classmate.types.ResolvedObjectType;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * 类型处理工具类
 * @author Frodez
 */
@UtilityClass
public class UType {

	private static final TypeResolver TYPE_RESOLVER = new TypeResolver();

	/**
	 * 解析类型
	 * @author Frodez
	 */
	public ResolvedType resolve(Type type) {
		return TYPE_RESOLVER.resolve(type);
	}

	/**
	 * 判断是否为无泛型类型
	 * @author Frodez
	 */
	public boolean isSimpleType(Type type) {
		return isSimpleType(TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 判断是否为无泛型类型
	 * @author Frodez
	 */
	public boolean isSimpleType(ResolvedType type) {
		return type instanceof ResolvedObjectType || type instanceof ResolvedPrimitiveType;
	}

	/**
	 * 判断是否为泛型类型
	 * @author Frodez
	 */
	public boolean isComplexType(Type type) {
		return isComplexType(TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 判断是否为泛型类型
	 * @author Frodez
	 */
	public boolean isComplexType(ResolvedType type) {
		return type instanceof ResolvedInterfaceType;
	}

	/**
	 * 判断是否为无泛型的某个类型或者其子类型
	 * @author Frodez
	 */
	public boolean belongToSimpleType(Class<?> simpleClass, Type type) {
		return belongToComplexType(simpleClass, TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 判断是否为无泛型的某个类型或者其子类型
	 * @author Frodez
	 */
	public boolean belongToSimpleType(Class<?> simpleClass, ResolvedType type) {
		return type instanceof ResolvedObjectType && simpleClass.isAssignableFrom(type.getErasedType());
	}

	/**
	 * 判断是否为带有泛型的某个类型或者其子类型
	 * @author Frodez
	 */
	public boolean belongToComplexType(Class<?> complexClass, Type type) {
		return belongToComplexType(complexClass, TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 判断是否为带有泛型的某个类型或者其子类型
	 * @author Frodez
	 */
	public boolean belongToComplexType(Class<?> complexClass, ResolvedType type) {
		return type instanceof ResolvedInterfaceType && complexClass.isAssignableFrom(type.getErasedType());
	}

	/**
	 * 获得具有两个泛型参数的类型的泛型参数具体类型
	 * @return A,B 泛型类型中A的类型和B的类型
	 * @author Frodez
	 */
	public List<ResolvedType> resolveGenericType(Class<?> complexClass, Type type) {
		return resolveGenericType(complexClass, TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 获得具有泛型参数的类型的泛型参数具体类型
	 * @return 泛型类型中泛型参数具体类型
	 * @author Frodez
	 */
	public List<ResolvedType> resolveGenericType(Class<?> complexClass, ResolvedType type) {
		if (complexClass.isAssignableFrom(type.getErasedType())) {
			return type.getTypeBindings().getTypeParameters();
		} else {
			return null;
		}
	}

	/**
	 * 判断是否为自动装箱涉及到的类型
	 * @author Frodez
	 */
	public static boolean isBoxType(Class<?> klass) {
		return klass == byte.class || klass == Byte.class || klass == int.class || klass == Integer.class || klass == boolean.class
			|| klass == Boolean.class || klass == long.class || klass == Long.class || klass == double.class || klass == Double.class
			|| klass == char.class || klass == Character.class || klass == float.class || klass == Float.class || klass == short.class
			|| klass == Short.class;
	}

	/**
	 * 类型是否为装箱拆箱所对应
	 * @author Frodez
	 */
	public static boolean isSameBoxType(Class<?> first, Class<?> second) {
		if (first == byte.class || first == Byte.class) {
			return second == byte.class || second == Byte.class;
		}
		if (first == int.class || first == Integer.class) {
			return second == int.class || second == Integer.class;
		}
		if (first == boolean.class || first == Boolean.class) {
			return second == boolean.class || second == Boolean.class;
		}
		if (first == long.class || first == Long.class) {
			return second == long.class || second == Long.class;
		}
		if (first == double.class || first == Double.class) {
			return second == double.class || second == Double.class;
		}
		if (first == char.class || first == Character.class) {
			return second == char.class || second == Character.class;
		}
		if (first == float.class || first == Float.class) {
			return second == float.class || second == Float.class;
		}
		if (first == short.class || first == Short.class) {
			return second == short.class || second == Short.class;
		}
		throw new IllegalArgumentException();
	}

}
