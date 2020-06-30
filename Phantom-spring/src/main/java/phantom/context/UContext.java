package phantom.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import phantom.common.UStream;
import phantom.common.UString;

/**
 * spring工具类
 * @author Frodez
 */
@Component
public class UContext implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
		Assert.notNull(context, "context must not be null");
	}

	/**
	 * 关闭spring应用,默认返回码1
	 * @author Frodez
	 */
	public static void exit() {
		exit(1);
	}

	/**
	 * 关闭spring应用
	 * @param exitCode 返回码
	 * @author Frodez
	 */
	public static void exit(int exitCode) {
		SpringApplication.exit(context, () -> exitCode);
	}

	/**
	 * 获取spring上下文环境
	 * @author Frodez
	 */
	public static ApplicationContext context() {
		if (context == null) {
			throw new IllegalStateException();
		}
		return context;
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param klass bean的类型
	 * @author Frodez
	 */
	public static <T> T bean(Class<T> klass) {
		return context.getBean(klass);
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param klass bean的类型
	 * @author Frodez
	 */
	public static <T> Map<String, T> beans(Class<T> klass) {
		return context.getBeansOfType(klass);
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param beanName bean的名字
	 * @param klass bean的类型
	 * @author Frodez
	 */
	@SuppressWarnings("unchecked")
	public static <T> T bean(String beanName, Class<T> klass) {
		return (T) context.getBean(beanName);
	}

	/**
	 * 根据ant风格模式字符串匹配路径,并获取路径下所有类
	 * @author Frodez
	 */
	@SneakyThrows
	public static List<Class<?>> classes(String pattern) {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		List<Resource> resources = UStream.filterList(resolver.getResources(getPackagePath(pattern)),
				Resource::isReadable);
		List<Class<?>> classes = new ArrayList<>();
		MetadataReaderFactory readerFactory = bean(MetadataReaderFactory.class);
		for (Resource resource : resources) {
			classes.add(ClassUtils.forName(readerFactory.getMetadataReader(resource).getClassMetadata().getClassName(),
					null));
		}
		return classes;
	}

	/**
	 * 根据ant风格模式字符串匹配路径,并获取路径下所有指定父类的类
	 * @author Frodez
	 */
	@SuppressWarnings("unchecked")
	@SneakyThrows
	public static <T> List<Class<? extends T>> classes(String pattern, Class<T> parent) {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		List<Resource> resources = UStream.filterList(resolver.getResources(getPackagePath(pattern)),
				Resource::isReadable);
		List<Class<? extends T>> classes = new ArrayList<>();
		MetadataReaderFactory readerFactory = bean(MetadataReaderFactory.class);
		for (Resource resource : resources) {
			Class<?> klass = ClassUtils
					.forName(readerFactory.getMetadataReader(resource).getClassMetadata().getClassName(), null);
			if (parent.isAssignableFrom(klass)) {
				classes.add((Class<? extends T>) klass);
			}
		}
		return classes;
	}

	/**
	 * 根据ant风格模式字符串匹配路径,并获取路径下所有满足要求的类
	 * @author Frodez
	 */
	@SneakyThrows
	public static List<Class<?>> classes(String pattern, Predicate<Class<?>> predicate) {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		List<Resource> resources = UStream.filterList(resolver.getResources(getPackagePath(pattern)),
				Resource::isReadable);
		List<Class<?>> classes = new ArrayList<>();
		MetadataReaderFactory readerFactory = bean(MetadataReaderFactory.class);
		for (Resource resource : resources) {
			Class<?> klass = ClassUtils
					.forName(readerFactory.getMetadataReader(resource).getClassMetadata().getClassName(), null);
			if (predicate.test(klass)) {
				classes.add(klass);
			}
		}
		return classes;
	}

	/**
	 * 转换成包名,ant风格
	 * @author Frodez
	 */
	public static String getPackagePath(String pattern) {
		String packagePath = ClassUtils
				.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(pattern));
		return UString.concat(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX, packagePath, "**/*.class");
	}

	/**
	 * 使用spring上下文环境获取所有端点,按请求方式分类(同一端点可能有多种请求方式)
	 * @author Frodez
	 */
	public static Map<RequestMethod, List<RequestMappingInfo>> endPoints() {
		Map<RequestMethod, List<RequestMappingInfo>> endPoints = new EnumMap<>(RequestMethod.class);
		Stream<RequestMappingHandlerMapping> stream = handlerMappingStream(RequestMappingHandlerMapping.class);
		for (RequestMethod method : RequestMethod.values()) {
			// 从spring上下文里拿出所有HandlerMapping
			// 遍历每种RequestMethod,找出它们对应的端点放入EnumMap
			endPoints.put(method, stream.map((iter) -> {
				// 取出所有的RequestMapping和对应的HandlerMethod,即@RequestMapping,@GetMapping,@PostMapping这些注解和它们所在的方法
				return iter.getHandlerMethods().keySet();
			}).flatMap(Collection::stream).filter((iter) -> {
				// 判断这个RequestMappingInfo的http RequestMethod是否是这次遍历所要寻找的RequestMethod
				return iter.getMethodsCondition().getMethods().contains(method);
			}).collect(Collectors.toList()));
		}
		return endPoints;
	}

	public static Collection<HandlerMapping> handlerMappings() {
		return BeanFactoryUtils.beansOfTypeIncludingAncestors(UContext.context(), HandlerMapping.class, true, false)
				.values();
	}

	@SuppressWarnings("unchecked")
	public static <T extends HandlerMapping> Stream<T> handlerMappingStream(Class<T> klass) {
		return handlerMappings().stream().filter((iter) -> klass.isAssignableFrom(iter.getClass()))
				.map((iter) -> (T) iter);
	}

}
