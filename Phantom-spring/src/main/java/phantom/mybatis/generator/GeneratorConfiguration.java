package phantom.mybatis.generator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import phantom.common.UString;
import phantom.mybatis.mapper.DataMapper;

/**
 * mybatis-generator生成器配置
 * @author Frodez
 */
@Data
public class GeneratorConfiguration {

	/**
	 * 基础mapper
	 */
	private Class<?> baseMapperClass = DataMapper.class;

	/**
	 * 表配置,一次可以填多项,但均位于同一个包下
	 */
	@NotEmpty
	private List<String> tables;

	/**
	 * 生成表的包名的后缀
	 */
	private String packageSuffix;

	/**
	 * 生成表的mapper包名的前缀
	 */
	@NotBlank
	private String mapperPackagePreffix;

	/**
	 * 生成表的模型包名的前缀
	 */
	@NotBlank
	private String modelPackagePreffix;

	/**
	 * 源代码文件夹的相对位置,根为项目目录
	 */
	@NotBlank
	private String folderPath = "src/main/java";

	/**
	 * 数据库地址
	 */
	@NotBlank
	private String url;

	/**
	 * 数据库名
	 */
	@NotBlank
	private String database;

	/**
	 * 数据库参数
	 */
	@NotNull
	private Map<String, String> dbParams;

	/**
	 * 用户名
	 */
	@NotBlank
	private String username;

	/**
	 * 密码
	 */
	@NotBlank
	private String password;

	/**
	 * 表名——实体名转换器
	 */
	@NotNull
	private Function<String, String> nameConverter = table -> {
		String[] strings = UString.split("_", table);
		if (table.startsWith("tb")) {
			return UString.concat(Stream.of(strings).skip(1).map(UString::upperFirst).collect(Collectors.toList()));
		} else {
			return UString.concat(Stream.of(strings).map(UString::upperFirst).collect(Collectors.toList()));
		}
	};

}
