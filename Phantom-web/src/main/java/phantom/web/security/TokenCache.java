package phantom.web.security;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * name-token缓存
 * @author Frodez
 */
@Component
public class TokenCache {

	private ConcurrentHashMap<String, String> ntMap = new ConcurrentHashMap<>();

	private ConcurrentHashMap<String, String> tnMap = new ConcurrentHashMap<>();

	/**
	 * 保存name和token
	 * @author Frodez
	 */
	public void save(String name, String token) {
		if (ntMap.containsKey(name) || tnMap.containsKey(token)) {
			throw new IllegalStateException("please save until that name and token are removed");
		}
		try {
			ntMap.put(name, token);
			tnMap.put(token, name);
		} catch (Exception e) {
			ntMap.remove(name);
			tnMap.remove(token);
			throw e;
		}
	}

	/**
	 * 根据name删除name和token,返回被删除的token
	 * @author Frodez
	 */
	public String removeByName(String name) {
		String token = ntMap.get(name);
		if (token == null) {
			return null;
		}
		if (!tnMap.containsKey(token)) {
			ntMap.remove(name);
			throw new IllegalStateException("thers exists name, but no token");
		}
		try {
			ntMap.remove(name);
			tnMap.remove(token);
			return token;
		} catch (Exception e) {
			ntMap.put(name, token);
			tnMap.put(token, name);
			throw e;
		}
	}

	/**
	 * 根据token删除name和token,返回被删除的name
	 * @author Frodez
	 */
	public String removeByToken(String token) {
		String name = tnMap.get(token);
		if (name == null) {
			return null;
		}
		if (!ntMap.containsKey(name)) {
			tnMap.remove(token);
			throw new IllegalStateException("thers exists token, but no name");
		}
		try {
			ntMap.remove(name);
			tnMap.remove(token);
			return name;
		} catch (Exception e) {
			ntMap.put(name, token);
			tnMap.put(token, name);
			throw e;
		}
	}

	/**
	 * 根据name获取token
	 * @author Frodez
	 */
	public String getToken(String name) {
		return ntMap.get(name);
	}

	/**
	 * 根据token获取name
	 * @author Frodez
	 */
	public String getName(String token) {
		return tnMap.get(token);
	}

	/**
	 * 根据name和token判断是否存在
	 * @author Frodez
	 */
	public boolean exist(String name, String token) {
		String resultToken = ntMap.get(name);
		String resultName = tnMap.get(token);
		return name.equals(resultName) && token.equals(resultToken);
	}

}
