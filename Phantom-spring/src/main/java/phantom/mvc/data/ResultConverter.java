package phantom.mvc.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import phantom.common.UString;
import phantom.mvc.data.Result.Value;
import phantom.tool.jackson.JSON;

/**
 * 自定义jacksonHttpMessageConverter(仅用于Result)
 * @author Frodez
 */
public class ResultConverter extends AbstractGenericHttpMessageConverter<Value<?>> {

	public ResultConverter() {
		setDefaultCharset(StandardCharsets.UTF_8);
		setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
		Assert.isTrue(JSON.mapper().canSerialize(Value.class), "Value can't be serialized!");
		Assert.isTrue(JSON.mapper().canDeserialize(JSON.mapper().getTypeFactory().constructType(Value.class)), "Value can't be deserialized!");
	}

	@Override
	protected Value<?> readInternal(Class<? extends Value<?>> clazz, HttpInputMessage inputMessage) throws IOException,
		HttpMessageNotReadableException {
		return Result.reader().readValue(inputMessage.getBody());
	}

	@Override
	public Value<?> read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException,
		HttpMessageNotReadableException {
		return Result.reader().readValue(inputMessage.getBody());
	}

	@Override
	protected void writeInternal(Value<?> object, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException,
		HttpMessageNotWritableException {
		try {
			OutputStream outputStream = outputMessage.getBody();
			//对通用Result采用特殊的优化过的方式
			byte[] cacheBytes = object.cache();
			if (cacheBytes != null) {
				outputStream.write(cacheBytes);
			} else {
				Result.writer().writeValue(outputStream, object);
			}
			outputStream.flush();
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException(UString.concat("Could not write JSON: ", ex.getOriginalMessage()), ex);
		}
	}

}
