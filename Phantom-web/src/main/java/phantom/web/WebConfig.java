package phantom.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import phantom.web.security.SecurityInterceptor;
import phantom.web.security.SecurityProperties;

/**
 * springMVC配置
 * @author Frodez
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private SecurityInterceptor interceptor;

	@Autowired
	private SecurityProperties properties;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor).addPathPatterns("/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		CorsRegistration configuration = registry.addMapping("/**");
		configuration.allowCredentials(true);
		List<String> origins = properties.getCors().getAllowedOrigins();
		List<String> methods = properties.getCors().getAllowedMethods();
		List<String> headers = properties.getCors().getAllowedHeaders();
		configuration.allowedOrigins(origins.toArray(new String[origins.size()]));
		configuration.allowedMethods(methods.toArray(new String[methods.size()]));
		configuration.allowedHeaders(headers.toArray(new String[headers.size()]));
	}

}
