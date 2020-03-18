package phantom.mybatis.result.handler;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.types.ResolvedObjectType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;
import phantom.mybatis.result.CustomHandler;
import phantom.reflect.UType;

/**
 * Map自定义ResultHandler<br>
 * 作用:按照key来聚合value。<br>
 * <strong> 使用须知:<br>
 * 1.resultType必须设置为java.util.HashMap!!!否则本Handler无效!<br>
 * 2.请在返回值字段名前添加key和value两种关键词,会与Map中的K和V相对应。关键词与原字段名间请用.号连接。<br>
 * 3.如果key或者value的字段只有一个,则可以直接用key或者value代替。<br>
 * </strong>
 * @author Frodez
 */
public class MapResultHandler implements CustomHandler {

	private MapResultHandlerContext context;

	private List<Map<Object, Object>> result;

	public MapResultHandler() {
		this.result = new ArrayList<>();
		this.result.add(new HashMap<>());
	}

	@Override
	public boolean support(List<ResultMap> resultMaps) {
		ResultMap map = resultMaps.get(0);
		return Map.class.isAssignableFrom(map.getType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleResult(ResultContext<? extends Object> resultContext) {
		Object resultObject = resultContext.getResultObject();
		if (Map.class.isAssignableFrom(resultObject.getClass())) {
			Map<Object, Object> resultMap = result.get(0);
			Map<String, Object> map = (Map<String, Object>) resultObject;
			Object key = context.key.transform(map.get("key"));
			Object value = context.value.transform(map.get("value"));
			Object object = resultMap.get(key);
			switch (context.valueType) {
				case OBJECT : {
					resultMap.put(key, value);
					break;
				}
				case SET : {
					if (object != null) {
						Set<Object> values = (Set<Object>) object;
						values.add(value);
					} else {
						Set<Object> values = new HashSet<>();
						values.add(value);
						resultMap.put(key, values);
					}
					break;
				}
				case LIST : {
					if (object != null) {
						List<Object> values = (List<Object>) object;
						values.add(value);
					} else {
						List<Object> values = new ArrayList<>();
						values.add(value);
						resultMap.put(key, values);
					}
					break;
				}
			}
		}
	}

	@Override
	public List<?> getResult() {
		return result;
	}

	@Override
	public void setContext(CustomHandlerContext context) {
		this.context = (MapResultHandlerContext) context;
	}

	@Override
	public CustomHandlerContext resolveContext(Method method, Configuration configuration) {
		ResolvedType resolvedType = UType.resolve(method.getGenericReturnType());
		List<ResolvedType> mapType = UType.resolveGenericType(Map.class, resolvedType);
		if (mapType == null) {
			error(resolvedType);
			return null;
		}
		ResolvedType keyType = mapType.get(0);
		ResolvedType valueType = mapType.get(1);
		if (!UType.isSimpleType(keyType)) {
			error(resolvedType);
			return null;
		}
		MapResultHandlerContext context = new MapResultHandlerContext();
		context.key = MapCondition.generate(keyType.getErasedType(), configuration);
		if (UType.isSimpleType(valueType)) {
			context.valueType = ValueType.OBJECT;
			context.value = MapCondition.generate(valueType.getErasedType(), configuration);
			return context;
		} else if (UType.belongToComplexType(List.class, valueType)) {
			context.valueType = ValueType.LIST;
			List<ResolvedType> genericTypes = UType.resolveGenericType(List.class, valueType);
			if (!(genericTypes.get(0) instanceof ResolvedObjectType)) {
				error(resolvedType);
				return null;
			}
			context.value = MapCondition.generate(genericTypes.get(0).getErasedType(), configuration);
			return context;
		} else if (UType.belongToComplexType(Set.class, valueType)) {
			context.valueType = ValueType.SET;
			List<ResolvedType> genericTypes = UType.resolveGenericType(Set.class, valueType);
			if (!(genericTypes.get(0) instanceof ResolvedObjectType)) {
				error(resolvedType);
				return null;
			}
			context.value = MapCondition.generate(genericTypes.get(0).getErasedType(), configuration);
			return context;
		} else {
			error(resolvedType);
			return null;
		}
	}

	private void error(ResolvedType resolvedType) {
		throw new IllegalArgumentException("不支持把" + resolvedType.getBriefDescription() + "转换为Map<K, Set<V>>,Map<K, List<V>>或者Map<K, V>!");
	}

	private class MapResultHandlerContext implements CustomHandlerContext {

		private ValueType valueType;

		private MapCondition key;

		private MapCondition value;

	}

	/**
	 * 支持的值类型
	 * @author Frodez
	 */
	private enum ValueType {

		/**
		 * 实体类
		 */
		OBJECT,
		/**
		 * set类型
		 */
		SET,
		/**
		 * list类型
		 */
		LIST;

	}

}
