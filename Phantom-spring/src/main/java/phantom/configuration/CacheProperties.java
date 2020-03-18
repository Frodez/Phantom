package phantom.configuration;

import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 缓存配置
 * @author Frodez
 */
@Data
@Component
@PropertySource(value = { "classpath:settings/global/cache.properties" })
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

	/**
	 * 默认单位:毫秒
	 */
	public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

	/**
	 * 标准配置
	 */
	private StandardProperties standard = new StandardProperties();

	/**
	 * redis配置
	 */
	private RedisProperties redis = new RedisProperties();

	@Data
	public static class StandardProperties {

		/**
		 * 超时时间
		 */
		private Integer timeout = 60000;

	}

	@Data
	public static class RedisProperties {

		/**
		 * 超时时间,单位分钟
		 */
		private Integer timeout = 7 * 1440;

	}

}
