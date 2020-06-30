package phantom.mybatis.mapper.exist;

import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class ExistMapperProvider extends MapperTemplate {

	public ExistMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String existByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CASE WHEN ");
		Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
		if (pkColumns.size() == 1) {
			sql.append("COUNT(").append(pkColumns.iterator().next().getColumn()).append(") ");
		} else {
			sql.append("COUNT(*) ");
		}
		sql.append(" = ${ids.size} THEN 1 ELSE 0 END AS result ");
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		if (pkColumns.size() == 1) {
			EntityColumn column = pkColumns.iterator().next();
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

	public String existEq(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CASE WHEN ");
		Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
		if (pkColumns.size() == 1) {
			sql.append("COUNT(").append(pkColumns.iterator().next().getColumn()).append(") ");
		} else {
			sql.append("COUNT(*) ");
		}
		sql.append(" = 0 THEN 0 ELSE 1 END AS result ");
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" = ");
		sql.append(" #{param}");
		return sql.toString();
	}

	public String existIn(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CASE WHEN ");
		Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
		if (pkColumns.size() == 1) {
			sql.append("COUNT(").append(pkColumns.iterator().next().getColumn()).append(") ");
		} else {
			sql.append("COUNT(*) ");
		}
		sql.append(" = 0 THEN 0 ELSE 1 END AS result ");
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" in ");
		sql.append(
				"<foreach collection=\"params\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
		sql.append(" #{item} ");
		sql.append("</foreach>");
		return sql.toString();
	}

}
