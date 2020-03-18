package phantom.configuration;

import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 代码检查配置
 * @author Frodez
 */
@Data
@Component
@PropertySource(value = { "classpath:settings/global/code-check.properties" })
@ConfigurationProperties(prefix = "validator")
public class CodeCheckProperties {

	/**
	 * 开启代码检查的环境名称
	 */
	private List<String> enviroments = Arrays.asList("dev", "test");

	/**
	 * 需要检查的代码,ant风格匹配
	 */
	private List<String> modelPath = Arrays.asList("phantom.*.*");

}
