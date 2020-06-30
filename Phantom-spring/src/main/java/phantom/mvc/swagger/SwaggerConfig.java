package phantom.mvc.swagger;

import com.fasterxml.classmate.TypeResolver;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import phantom.configuration.SwaggerProperties;
import phantom.mvc.data.PageData;
import phantom.mvc.data.QueryPage;
import phantom.mvc.data.Result;
import phantom.mvc.data.Result.State;
import phantom.mvc.data.Result.Value;
import phantom.reflect.UReflect;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * swagger配置
 * @author Frodez
 */
@EnableSwagger2WebMvc
@Configuration
@Profile({ "dev", "test" })
public class SwaggerConfig {

	/**
	 * swagger参数配置
	 */
	@Autowired
	private SwaggerProperties swagger;

	/**
	 * 主配置
	 * @author Frodez
	 */
	@Bean
	public Docket enable() {
		Docket docket = baseConfig();
		infoConfig(docket);
		typeConfig(docket);
		modelConfig(docket);
		genericTypeNamingStrategyConfig(docket);
		return docket;
	}

	/**
	 * 基本配置
	 * @author Frodez
	 */
	private Docket baseConfig() {
		ApiSelectorBuilder selectorBuilder = new Docket(DocumentationType.SWAGGER_2).select();
		selectorBuilder.apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()));
		selectorBuilder.paths(PathSelectors.any());
		return selectorBuilder.build();
	}

	/**
	 * 说明配置
	 * @author Frodez
	 */
	private void infoConfig(Docket docket) {
		ApiInfoBuilder infoBuilder = new ApiInfoBuilder();
		infoBuilder.title(swagger.getTitle());
		infoBuilder.description(swagger.getDescription());
		Contact contact = new Contact(swagger.getAuthor(), swagger.getDocUrl(), swagger.getEmail());
		infoBuilder.contact(contact);
		infoBuilder.version(swagger.getAppVersion());
		infoBuilder.license(swagger.getLicense());
		infoBuilder.licenseUrl(swagger.getLicenseUrl());
		docket.apiInfo(infoBuilder.build());
	}

	/**
	 * 类型映射配置
	 * @author Frodez
	 */
	private void typeConfig(Docket docket) {
		// 将模型重定向
		docket.directModelSubstitute(LocalDate.class, Long.class);
		docket.directModelSubstitute(LocalDateTime.class, Long.class);
		docket.directModelSubstitute(LocalTime.class, Long.class);
		docket.directModelSubstitute(Date.class, Long.class);
		TypeResolver resolver = new TypeResolver();
		// 有扫描包之外的模型,则在此配置
		docket.additionalModels(resolver.resolve(QueryPage.class));
		docket.additionalModels(resolver.resolve(PageData.class));
		docket.additionalModels(resolver.resolve(State.class));
		docket.additionalModels(resolver.resolve(Value.class));
	}

	/**
	 * 模型映射配置
	 * @author Frodez
	 */
	private void modelConfig(Docket docket) {
		List<ResponseMessage> responseMessageList = getGlobalResponseMessage();
		docket.useDefaultResponseMessages(false);
		docket.enableUrlTemplating(false);
		docket.globalResponseMessage(RequestMethod.GET, responseMessageList);
		docket.globalResponseMessage(RequestMethod.POST, responseMessageList);
		docket.globalResponseMessage(RequestMethod.PUT, responseMessageList);
		docket.globalResponseMessage(RequestMethod.DELETE, responseMessageList);
	}

	/**
	 * 命名逻辑配置
	 * @author Frodez
	 */
	@SneakyThrows
	private void genericTypeNamingStrategyConfig(Docket docket) {
		UReflect.set(Docket.class, "genericsNamingStrategy", docket, new GenericTypeNamingStrategy() {

			private static final String OPEN = "<";

			private static final String CLOSE = ">";

			private static final String DELIM = ",";

			@Override
			public String getOpenGeneric() {
				return OPEN;
			}

			@Override
			public String getCloseGeneric() {
				return CLOSE;
			}

			@Override
			public String getTypeListDelimiter() {
				return DELIM;
			}
		});
	}

	/**
	 * 全局返回信息
	 * @author Frodez
	 */
	private List<ResponseMessage> getGlobalResponseMessage() {
		List<ResponseMessage> list = new ArrayList<>();
		Map<HttpStatus, List<Result.ResultEnum>> map = new HashMap<>();
		for (Result.ResultEnum item : Result.ResultEnum.values()) {
			/*
			 * if (item.status == HttpStatus.OK) { //成功的返回信息不设置默认 continue; }
			 */
			if (map.containsKey(item.status)) {
				map.get(item.status).add(item);
			} else {
				List<Result.ResultEnum> enumList = new ArrayList<>();
				enumList.add(item);
				map.put(item.status, enumList);
			}
		}
		for (Entry<HttpStatus, List<Result.ResultEnum>> entry : map.entrySet()) {
			String message = String.join(" | ",
					entry.getValue().stream().map((iter) -> iter.desc + ",自定义状态码:" + iter.val).collect(Collectors
							.toList()));
			ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder();
			messageBuilder.code(entry.getKey().value());
			messageBuilder.message(message);
			messageBuilder.responseModel(new ModelRef(Value.class.getSimpleName()));
			list.add(messageBuilder.build());
		}
		return list;
	}

}
