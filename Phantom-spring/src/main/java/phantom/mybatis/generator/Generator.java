package phantom.mybatis.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import com.mysql.cj.jdbc.Driver;

import lombok.SneakyThrows;
import phantom.common.UString;
import phantom.tool.validate.Validate;

/**
 * mybatis-generator生成器<br>
 * 不用再担心各种奇奇怪怪的路径问题,无法加载类问题<br>
 * 可以方便地自定义规则<br>
 * @author Frodez
 */
public class Generator {

	/**
	 * 流程
	 * @author Frodez
	 */
	@SneakyThrows
	public static void run(final GeneratorConfiguration param) {
		Validate.assertValid(param);
		CustomSettingsPlugin.MAPPER_NAME = param.getBaseMapperClass().getCanonicalName();
		// if(UEmpty)
		final List<String> warnings = new ArrayList<>();
		final Configuration configuration = config(param);
		final MyBatisGenerator generator = new MyBatisGenerator(configuration, new DefaultShellCallback(true),
				warnings);
		generator.generate(new VerboseProgressCallback());
		System.out.println(String.join("\n", warnings));
	}

	/**
	 * 配置
	 * @author Frodez
	 */
	@SneakyThrows
	private static Configuration config(final GeneratorConfiguration param) {
		final Configuration configuration = new Configuration();
		final Context context = new Context(ModelType.FLAT);
		// id
		context.setId("mysql");
		context.setTargetRuntime("MyBatis3Simple");
		// 额外属性
		context.addProperty("beginningDelimiter", "`");
		context.addProperty("endingDelimiter", "`");
		context.addProperty("javaFileEncoding", "utf-8");
		context.addProperty("javaFormatter", DefaultJavaFormatter.class.getCanonicalName());
		context.addProperty("xmlFormatter", CustomXmlFormatter.class.getCanonicalName());
		// 插件
		final PluginConfiguration pluginConfiguration = new PluginConfiguration();
		pluginConfiguration.setConfigurationType(CustomSettingsPlugin.class.getCanonicalName());
		context.addPluginConfiguration(pluginConfiguration);
		// 注释生成
		final CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
		commentGeneratorConfiguration.setConfigurationType(CustomCommentGenerator.class.getCanonicalName());
		commentGeneratorConfiguration.addProperty("suppressAllComments", "true");
		commentGeneratorConfiguration.addProperty("suppressDate", "true");
		context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
		// jdbc连接(这样可以避免专门获取数据库连接用的jar包)
		final JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
		jdbcConnectionConfiguration.setDriverClass(Driver.class.getCanonicalName());
		String url = param.getUrl() + "/" + param.getDatabase();
		final Map<String, String> dbParams = param.getDbParams();
		if (!dbParams.isEmpty()) {
			url = url + "?"
					+ String.join("&",
							dbParams.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
									.collect(Collectors
											.toList()));
		}
		jdbcConnectionConfiguration.setConnectionURL(url);
		jdbcConnectionConfiguration.setUserId(param.getUsername());
		jdbcConnectionConfiguration.setPassword(param.getPassword());
		context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
		// 类型映射
		final JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
		javaTypeResolverConfiguration.setConfigurationType(JavaTypeResolverDefaultImpl.class.getCanonicalName());
		javaTypeResolverConfiguration.addProperty("forceBigDecimals", "true");
		context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
		// java-model
		final String modelPackage = UString.join(".", param.getModelPackagePreffix(), param.getPackageSuffix());
		final JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
		javaModelGeneratorConfiguration.setTargetPackage(modelPackage);
		javaModelGeneratorConfiguration.setTargetProject(param.getFolderPath());
		context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
		// mapper.xml
		final String mapperPackage = UString.join(".", param.getMapperPackagePreffix(), param.getPackageSuffix());
		final SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
		sqlMapGeneratorConfiguration.setTargetPackage(mapperPackage);
		sqlMapGeneratorConfiguration.setTargetProject(param.getFolderPath());
		context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
		// mapper
		final JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
		javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
		javaClientGeneratorConfiguration.setTargetPackage(mapperPackage);
		javaClientGeneratorConfiguration.setTargetProject(param.getFolderPath());
		context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
		// 数据库表
		for (final String table : param.getTables()) {
			final TableConfiguration tableConfiguration = new TableConfiguration(context);
			tableConfiguration.setTableName(table);
			final String domainName = param.getNameConverter().apply(table);
			tableConfiguration.setDomainObjectName(domainName);
			tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
			context.addTableConfiguration(tableConfiguration);
		}
		configuration.addContext(context);
		// 验证参数正确性
		configuration.validate();
		return configuration;
	}

}
