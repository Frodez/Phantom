package phantom.context;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import phantom.configuration.AsyncProperties;

/**
 * 异步配置
 * @author Frodez
 */
@Slf4j
@EnableAsync
@Configuration
@EnableScheduling
public class AsyncConfig extends AsyncConfigurerSupport {

	@Autowired
	@Getter
	private AsyncProperties properties;

	@Override
	public AsyncTaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		int corePoolSize = Math.round(availableProcessors * properties.getCoreThreadTimes());
		int maxPoolSize = Math.round(availableProcessors * properties.getMaxThreadTimes());
		int queueCapacity = Math.round(maxPoolSize * properties.getQueueFactors());
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
		executor.setThreadNamePrefix(properties.getThreadNamePrefix());
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		log.info("async executor is already now!");
		log.info("async config:corePoolSize-{}, maxPoolSize-{}, queueCapacity-{}, keepAliveSeconds-{}, threadNamePrefix-{}", corePoolSize,
			maxPoolSize, queueCapacity, properties.getKeepAliveSeconds(), properties.getThreadNamePrefix());
		return executor;
	}

}
