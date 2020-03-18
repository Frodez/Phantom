package phantom.aop.request.checker;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import phantom.configuration.CacheProperties;

/**
 * 阻塞型重复请求检查GUAVACACHE实现
 * @author Frodez
 */
@Component
public class ManualGuavaChecker implements ManualChecker {

	private Cache<String, Boolean> cache;

	@Autowired
	public ManualGuavaChecker(CacheProperties properties) {
		cache = CacheBuilder.newBuilder().expireAfterAccess(properties.getStandard().getTimeout(), CacheProperties.TIME_UNIT).build();
	}

	@Override
	public boolean check(String key) {
		return cache.getIfPresent(key) != null;
	}

	@Override
	public void lock(String key) {
		cache.put(key, true);
	}

	@Override
	public void free(String key) {
		cache.invalidate(key);
	}

}
