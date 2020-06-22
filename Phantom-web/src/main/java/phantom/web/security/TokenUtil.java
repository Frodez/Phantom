package phantom.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import phantom.common.Pair;
import phantom.constant.UserType;
import phantom.context.StaticInitProcessor.StaticInit;
import phantom.context.UContext;

/**
 * token工具类
 * @author Frodez
 */
@Component
@StaticInit
public class TokenUtil {

	/**
	 * 算法
	 */
	private static Algorithm algorithm;

	/**
	 * 验证过期
	 */
	private static JWTVerifier verifier;

	/**
	 * 不验证过期
	 */
	private static JWTVerifier expiredVerifier;

	/**
	 * 签发者
	 */
	private static String issuer;

	/**
	 * 是否过期，true为会过期
	 */
	private static boolean expired = false;

	/**
	 * 过期时长(毫秒)
	 */
	private static Long expiration;

	/**
	 * 声明
	 */
	private static String authorityClaim;

	/**
	 * HttpHeader名称
	 */
	private static String header;

	/**
	 * token前缀
	 */
	private static String tokenPrefix;

	/**
	 * token前缀长度
	 */
	private static int tokenPrefixLength;

	@PostConstruct
	private void init() {
		SecurityProperties properties = UContext.bean(SecurityProperties.class);
		algorithm = Algorithm.HMAC256(properties.getJwt().getSecret());
		issuer = properties.getJwt().getIssuer();
		expiration = properties.getJwt().getExpiration() * 1000;
		expired = expiration > 0;
		authorityClaim = properties.getJwt().getAuthorityClaim();
		header = properties.getJwt().getHeader();
		tokenPrefix = properties.getJwt().getTokenPrefix();
		tokenPrefixLength = tokenPrefix.length();
		verifier = JWT.require(algorithm).withIssuer(issuer).build();
		expiredVerifier = JWT.require(algorithm).acceptExpiresAt(0).withIssuer(issuer).build();
		Assert.notNull(algorithm, "algorithm must not be null");
		Assert.notNull(issuer, "issuer must not be null");
		Assert.notNull(expiration, "expiration must not be null");
		Assert.notNull(authorityClaim, "authorityClaim must not be null");
		Assert.notNull(header, "header must not be null");
		Assert.notNull(tokenPrefix, "tokenPrefix must not be null");
		Assert.notNull(verifier, "verifier must not be null");
		Assert.notNull(expiredVerifier, "expireAbleVerifier must not be null");
	}

	/**
	 * 基本builder
	 * @author Frodez
	 */
	private static Builder baseBuilder() {
		long now = System.currentTimeMillis();
		Builder builder = JWT.create();
		builder.withIssuer(issuer);
		builder.withIssuedAt(new Date(now));
		if (expired) {
			builder.withExpiresAt(new Date(now + expiration));
		}
		return builder;
	}

	/**
	 * 生成token
	 * @author Frodez
	 * @param UserEndpointDetail 用户信息
	 */
	public static String generate(String username, UserType userType) {
		Builder builder = baseBuilder();
		builder.withSubject(username);
		builder.withClaim(authorityClaim, userType.name());
		return builder.sign(algorithm);
	}

	/**
	 * 验证token
	 * @author Frodez
	 * @param token
	 */
	public static Pair<String, UserType> verify(String token) {
		//前面已经将exp置为合适的过期时间了,这里只需要判断其是否超过当前时间即可.
		DecodedJWT jwt = expired ? expiredVerifier.verify(token) : verifier.verify(token);
		String username = jwt.getSubject();
		String type = jwt.getClaim(authorityClaim).asString();
		UserType userType = UserType.valueOf(type);
		if (userType == null) {
			throw new IllegalArgumentException("illegal user_type:" + type);
		}
		return new Pair<>(username, userType);
	}

	/**
	 * 验证token,且一定考虑过期
	 * @author Frodez
	 */
	public static Pair<String, UserType> verifyWithNoExpired(String token) {
		DecodedJWT jwt = verifier.verify(token);
		String username = jwt.getSubject();
		String type = jwt.getClaim(authorityClaim).asString();
		UserType userType = UserType.valueOf(type);
		if (userType == null) {
			throw new IllegalArgumentException("illegal user_type:" + type);
		}
		return new Pair<>(username, userType);
	}

	/**
	 * 获取request中的token,如果为空或者前缀不符合设置,均返回null.
	 * @author Frodez
	 */
	public static String getRealToken(HttpServletRequest request) {
		String token = request.getHeader(header);
		if (token == null || !token.startsWith(tokenPrefix)) {
			return null;
		}
		return token.substring(tokenPrefixLength);
	}

	/**
	 * 获取request中的token,不做任何处理.
	 * @author Frodez
	 */
	public static String getFullToken(HttpServletRequest request) {
		return request.getHeader(header);
	}

}
