package phantom.aop.request.checker;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import phantom.configuration.CacheProperties;

/**
 * 自动超时型重复请求检查GUAVACACHE实现
 * @author Frodez
 */
@Component
public class AutoGuavaChecker implements AutoChecker {

	private Cache<String, Long> cache;

	@Autowired
	public AutoGuavaChecker(CacheProperties properties) {
		cache = CacheBuilder.newBuilder().expireAfterAccess(properties.getStandard().getTimeout(), CacheProperties.TIME_UNIT).build();
	}

	@Override
	public boolean check(String key) {
		Long timestamp = cache.getIfPresent(key);
		long now = System.currentTimeMillis();
		if (timestamp == null) {
			return false;
		} else if (timestamp < now) {
			cache.invalidate(key);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void lock(String key, long timeout) {
		cache.put(key, timeout + System.currentTimeMillis());
	}

}
