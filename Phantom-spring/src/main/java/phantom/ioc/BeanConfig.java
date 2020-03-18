package phantom.ioc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import phantom.tool.jackson.JSON;

@Configuration
public class BeanConfig {

	@Bean
	ObjectMapper objectMapper() {
		return JSON.mapper();
	}

	@Bean
	public StringRedisTemplate StringRedisTemplate(RedisConnectionFactory connectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setStringSerializer(StringRedisSerializer.UTF_8);
		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();
		return template;
	}

}
