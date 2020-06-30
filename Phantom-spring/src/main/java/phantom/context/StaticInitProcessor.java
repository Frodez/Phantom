package phantom.context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 使用该处理器完成静态初始化
 * @author Frodez
 */
@Component
public class StaticInitProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryAware {

	/**
	 * 需要静态初始化的请加入本注解
	 * @author Frodez
	 */
	@Documented
	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface StaticInit {

	}

	private ConfigurableListableBeanFactory beanFactory;

	Boolean prepared = false;

	private void prepare() {
		Assert.notNull(beanFactory.getBean(UContext.class), "UContext must not be null!");
		Assert.notNull(beanFactory.getBean(UProperty.class), "UProperty must not be null!");
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
			throw new IllegalArgumentException(
					"StaticInitProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
		}
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	@Override
	@Nullable
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (!bean.getClass().isAnnotationPresent(StaticInit.class)) {
			return bean;
		}
		synchronized (prepared) {
			if (!prepared) {
				prepare();
				prepared = true;
			}
		}
		return bean;
	}

}
