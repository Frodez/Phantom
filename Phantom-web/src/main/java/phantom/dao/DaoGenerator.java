package phantom.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import phantom.mybatis.generator.Generator;
import phantom.mybatis.generator.GeneratorConfiguration;

/**
 * mybatis生成器
 * @author Frodez
 */
public class DaoGenerator {

	public static void main(String[] args) {
		GeneratorConfiguration param = new GeneratorConfiguration();
		param.setTables(Arrays.asList("tb_user"));
		param.setModelPackagePreffix("phantom.dao.model");
		param.setMapperPackagePreffix("phantom.dao.mapper");
		param.setPackageSuffix("");
		param.setUrl("jdbc:mysql://127.0.0.1:3306");
		Map<String, String> dbParam = new HashMap<>();
		dbParam.put("useUnicode", "true");
		dbParam.put("characterEncoding", "utf8");
		dbParam.put("serverTimezone", "GMT%2B8");
		dbParam.put("useSSL", "false");
		param.setDbParams(dbParam);
		param.setDatabase("blog");
		param.setUsername("frodez");
		param.setPassword("123456");
		Generator.run(param);
	}

}
