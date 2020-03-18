package phantom.code;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import phantom.code.annotation.Checkable;
import phantom.code.checker.CodeChecker;
import phantom.configuration.CodeCheckProperties;
import phantom.ioc.UContext;

/**
 * 代码检查执行器<br>
 * 在validator相关配置中配置开启检查,则会对指定的实体进行类型检查.<br>
 * @author Frodez
 */
@Slf4j
@Component
public class CodeCheckExecuator implements ApplicationListener<ApplicationStartedEvent> {

	@Autowired
	private CodeCheckProperties properties;

	@Autowired
	private CodeChecker codeChecker;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		try {
			if (codeChecker.needCheck()) {
				log.info("代码校验开始");
				check();
				log.info("代码校验结束");
			} else {
				log.info("未开启代码校验功能");
			}
		} catch (IOException | ClassNotFoundException | LinkageError e) {
			log.error("发生错误,程序终止", e);
			UContext.exit();
		}
	}

	/**
	 * 执行检查
	 * @author Frodez
	 */
	private void check() throws IOException, ClassNotFoundException, LinkageError {
		for (String path : properties.getModelPath()) {
			for (Class<?> klass : UContext.classes(path)) {
				if (klass.getAnnotation(Checkable.class) != null) {
					codeChecker.checkClass(klass);
				}
			}
		}
	}

}
