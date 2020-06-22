package phantom.web.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import lombok.experimental.UtilityClass;

/**
 * 密码工具类
 * @author Frodez
 */
@UtilityClass
public class SecretUtil {

	private static Verifyer verifyer = BCrypt.verifyer();

	private static Hasher hasher = BCrypt.withDefaults();

	/**
	 * 判断是否匹配
	 * @author Frodez
	 */
	public static boolean match(String raw, String secret) {
		return verifyer.verify(raw.toCharArray(), secret).verified;
	}

	/**
	 * 加密
	 * @author Frodez
	 */
	public static String generate(String raw) {
		return hasher.hashToString(12, raw.toCharArray());
	}

}
