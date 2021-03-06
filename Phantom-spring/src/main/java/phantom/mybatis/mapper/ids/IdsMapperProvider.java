package phantom.mybatis.mapper.ids;

import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class IdsMapperProvider extends MapperTemplate {

	public IdsMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String selectByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 将返回值修改为实体类型
		setResultType(ms, entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
		if (columnList.size() == 1) {
			EntityColumn column = columnList.iterator().next();
			sql.append(" where ");
			sql.append(column.getColumn());
			sql.append(" in ");
			sql.append(
					"<foreach collection=\"ids\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
			sql.append(" #{item} ");
			sql.append("</foreach>");
		} else {
			throw new MapperException(
					"继承 selectByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须有且只有一个带有 @Id 注解的字段");
		}
		return sql.toString();
	}

	public String deleteByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
		Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
		if (columnList.size() == 1) {
			EntityColumn column = columnList.iterator().next();
			sql.append(" where ");
			sql.append(column.getColumn());
			sql.append(" in ");
			sql.append(
					"<foreach collection=\"ids\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
			sql.append(" #{item} ");
			sql.append("</foreach>");
		} else {
			throw new MapperException(
					"继承 deleteByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须有且只有一个带有 @Id 注解的字段");
		}
		return sql.toString();
	}

}
