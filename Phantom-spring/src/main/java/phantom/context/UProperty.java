package phantom.context;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import phantom.common.UEmpty;
import phantom.context.StaticInitProcessor.StaticInit;

/**
 * 访问控制参数配置
 * @author Frodez
 */
@Component
@StaticInit
public class UProperty {

	/**
	 * spring环境参数配置
	 */
	private static Environment env;

	private static List<String> activeEnvs;

	@PostConstruct
	private void init() {
		env = UContext.bean(Environment.class);
		activeEnvs = List.of(env.getActiveProfiles());
		Assert.notNull(env, "env must not be null");
		Assert.notNull(activeEnvs, "activeEnvs must not be null");
	}

	/**
	 * 根据key获取配置
	 * @author Frodez
	 */
	public static String get(String key) {
		return env.getProperty(key);
	}

	/**
	 * 根据key获取配置,获取失败返回默认值
	 * @author Frodez
	 */
	public static String get(String key, String defaultValue) {
		return env.getProperty(key, defaultValue);
	}

	/**
	 * 获取当前激活的配置版本
	 * @author Frodez
	 */
	public static List<String> activeProfiles() {
		return activeEnvs;
	}

	/**
	 * 判断是否与激活的配置匹配
	 * @author Frodez
	 */
	public static boolean matchEnvs(List<String> enviroments) {
		if (UEmpty.yes(enviroments)) {
			throw new IllegalArgumentException("需要匹配的环境不能为空!");
		}
		return enviroments.stream().anyMatch((env) -> {
			return activeEnvs.contains(env);
		});
	}

	/**
	 * 判断是否与激活的配置匹配
	 * @author Frodez
	 */
	public static boolean matchEnvs(String... enviroments) {
		if (UEmpty.yes(enviroments)) {
			throw new IllegalArgumentException("需要匹配的环境不能为空!");
		}
		return Arrays.stream(enviroments).anyMatch((env) -> {
			return activeEnvs.contains(env);
		});
	}

}
