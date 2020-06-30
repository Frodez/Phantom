package phantom.mvc.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import phantom.common.UString;
import phantom.mvc.data.Result.Value;
import phantom.reflect.MultiTypeKey;
import phantom.tool.jackson.JSON;

/**
 * 自定义jacksonHttpMessageConverter(仅用于非Result)
 * @author Frodez
 */
public class JsonConverter extends AbstractGenericHttpMessageConverter<Object> {

	private Map<MultiTypeKey, Boolean> deserializeCache = new ConcurrentHashMap<>();

	private Map<Class<?>, Boolean> serializeCache = new ConcurrentHashMap<>();

	public JsonConverter() {
		setDefaultCharset(StandardCharsets.UTF_8);
		setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
	}

	@Override
	public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
		return canRead(clazz, null, mediaType);
	}

	@Override
	public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {
		if (!canRead(mediaType)) {
			return false;
		}
		if (type instanceof Class<?> && Value.class.isAssignableFrom((Class<?>) type)) {
			return false;
		}
		MultiTypeKey typeKey = new MultiTypeKey(type, contextClass);
		Boolean cacheResult = deserializeCache.get(typeKey);
		if (cacheResult != null) {
			return cacheResult;
		}
		JavaType javaType = JSON.mapper().getTypeFactory()
				.constructType(GenericTypeResolver.resolveType(type, contextClass));
		AtomicReference<Throwable> causeRef = new AtomicReference<>();
		cacheResult = JSON.mapper().canDeserialize(javaType, causeRef);
		logWarningIfNecessary(javaType, causeRef.get());
		deserializeCache.put(typeKey, cacheResult);
		return cacheResult;
	}

	@Override
	public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
		if (clazz == Result.class || !canWrite(mediaType)) {
			return false;
		}
		Boolean cacheResult = serializeCache.get(clazz);
		if (cacheResult != null) {
			return cacheResult;
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<>();
		cacheResult = JSON.mapper().canSerialize(clazz, causeRef);
		logWarningIfNecessary(clazz, causeRef.get());
		serializeCache.put(clazz, cacheResult);
		return cacheResult;
	}

	/**
	 * Determine whether to log the given exception coming from a
	 * {@link ObjectMapper#canDeserialize} / {@link ObjectMapper#canSerialize}
	 * check.
	 * @param type the class that Jackson tested for (de-)serializability
	 * @param cause the Jackson-thrown exception to evaluate (typically a
	 *            {@link JsonMappingException})
	 * @since 4.3
	 */
	private void logWarningIfNecessary(Type type, @Nullable Throwable cause) {
		if (cause == null) {
			return;
		}
		// Do not log warning for serializer not found (note: different message wording
		// on Jackson 2.9)
		boolean debugLevel = cause instanceof JsonMappingException && cause.getMessage().startsWith("Cannot find");
		if (debugLevel ? logger.isDebugEnabled() : logger.isWarnEnabled()) {
			String msg = UString.concat("Failed to evaluate Jackson ", type instanceof JavaType ? "de" : "",
					"serialization for type [", type
							.toString(),
					"]");
			if (debugLevel) {
				logger.debug(msg, cause);
			} else if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			} else {
				logger.warn(msg + ": " + cause);
			}
		}
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return JSON.as(inputMessage.getBody(), clazz);
	}

	@Override
	public Object read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		return JSON.as(inputMessage.getBody(), GenericTypeResolver.resolveType(type, contextClass));
	}

	@Override
	protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage)
			throws IOException,
			HttpMessageNotWritableException {
		try {
			OutputStream outputStream = outputMessage.getBody();
			JSON.writer(object).writeValue(outputStream, object);
			outputStream.flush();
		} catch (InvalidDefinitionException ex) {
			throw new HttpMessageConversionException(UString.concat("Type definition error: ", ex.getType().toString()),
					ex);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException(UString.concat("Could not write JSON: ", ex.getOriginalMessage()),
					ex);
		}
	}

}
