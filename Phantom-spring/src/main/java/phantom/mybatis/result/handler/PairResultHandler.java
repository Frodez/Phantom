package phantom.mybatis.result.handler;

import com.fasterxml.classmate.ResolvedType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;
import phantom.common.Pair;
import phantom.mybatis.result.CustomHandler;
import phantom.reflect.UType;

/**
 * Pair自定义ResultHandler<br>
 * 如果有时需要两个实体联查,可以使用本ResultHandler.<br>
 * <strong> 使用须知:<br>
 * 1.resultType必须设置为java.util.HashMap!!!否则本Handler无效!<br>
 * 2.请在返回值字段名前添加key和value两种关键词,会与Pair中的K和V相对应。关键词与原字段名间请用.号连接。<br>
 * 3.如果key或者value的字段只有一个,则可以直接用key或者value代替。<br>
 * </strong>
 * @author Frodez
 */
public class PairResultHandler implements CustomHandler {

	private PairResultHandlerContext context;

	private List<Pair<Object, Object>> many = new ArrayList<>();

	@Override
	public boolean support(List<ResultMap> resultMaps) {
		ResultMap map = resultMaps.get(0);
		return Map.class.isAssignableFrom(map.getType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleResult(ResultContext<? extends Object> resultContext) {
		Object object = resultContext.getResultObject();
		if (Map.class.isAssignableFrom(object.getClass())) {
			Map<String, Object> map = (Map<String, Object>) object;
			Object key = context.key.transform(map.get("key"));
			Object value = context.value.transform(map.get("value"));
			Pair<Object, Object> pair = new Pair<>(key, value);
			many.add(pair);
		}
	}

	@Override
	public List<?> getResult() {
		return many;
	}

	/**
	 * 设置上下文
	 * @author Frodez
	 */
	@Override
	public void setContext(CustomHandlerContext context) {
		this.context = (PairResultHandlerContext) context;
	}

	/**
	 * 获取泛型的具体类型
	 * @author Frodez
	 */
	@Override
	public CustomHandlerContext resolveContext(Method method, Configuration configuration) {
		ResolvedType resolvedType = UType.resolve(method.getGenericReturnType());
		PairResultHandlerContext context = new PairResultHandlerContext();
		ResolvedType keyType;
		ResolvedType valueType;
		if (UType.belongToSimpleType(Pair.class, resolvedType)) {
			List<ResolvedType> pairType = UType.resolveGenericType(Pair.class, resolvedType);
			keyType = pairType.get(0);
			valueType = pairType.get(1);
			if (!UType.isSimpleType(keyType) || !UType.isSimpleType(valueType)) {
				error(resolvedType);
				return null;
			}
		} else if (UType.belongToComplexType(List.class, resolvedType)) {
			resolvedType = UType.resolveGenericType(List.class, resolvedType).get(0);
			if (!UType.belongToSimpleType(Pair.class, resolvedType)) {
				error(resolvedType);
				return null;
			}
			List<ResolvedType> pairType = UType.resolveGenericType(Pair.class, resolvedType);
			keyType = pairType.get(0);
			valueType = pairType.get(1);
			if (!UType.isSimpleType(keyType) || !UType.isSimpleType(valueType)) {
				error(resolvedType);
				return null;
			}
		} else {
			error(resolvedType);
			return null;
		}
		context.key = MapCondition.generate(keyType.getErasedType(), configuration);
		context.value = MapCondition.generate(valueType.getErasedType(), configuration);
		return context;
	}

	private void error(ResolvedType resolvedType) {
		throw new IllegalArgumentException("不支持把" + resolvedType.getBriefDescription() + "转换为Pair<K,V>或者List<Pair<K,V>>!");
	}

	private class PairResultHandlerContext implements CustomHandlerContext {

		private MapCondition key;

		private MapCondition value;

	}

}
