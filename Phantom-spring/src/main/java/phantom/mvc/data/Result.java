package phantom.mvc.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import phantom.common.UString;
import phantom.tool.jackson.JSON;

/**
 * 通用结果工具类<br>
 * <br>
 * 注意事项:<br>
 * <strong><br>
 * 1.配置jackson等json转换工具时,请务必将字段解析层级调至私有字段级别。有的工具默认使用getter和setter方法解析字段,此时无法解析数据字段。<br>
 * 例如jackson中可以使用如下配置: <br>
 * </strong>
 *
 * <pre>
 * mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
 * </pre>
 *
 * @author Frodez
 */
@UtilityClass
public class Result {

	/**
	 * 只包含状态和消息的获取值
	 * @author Frodez
	 */
	@SuppressWarnings("rawtypes")
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	public static class State extends Value implements Serializable {

		private static final long serialVersionUID = 1L;

		/**
		 * 不允许使用本方法
		 * @author Frodez
		 */
		@Override
		public Object get() {
			throw new StateException(errorMsg());
		}

		/**
		 * 不允许使用本方法
		 * @author Frodez
		 */
		@Override
		public Object or(Object other) {
			throw new StateException(errorMsg());
		}

		/**
		 * 不允许使用本方法
		 * @author Frodez
		 */
		@Override
		public State present() {
			throw new StateException(errorMsg());
		}

		/**
		 * 不允许使用本方法
		 * @author Frodez
		 */
		@Override
		public Optional<?> optional() {
			throw new StateException(errorMsg());
		}

		private State() {

		}

		private State(ResultEnum status) {
			super(status);
		}

		private State(String message, ResultEnum status) {
			super(message, status);
		}

		@Override
		public State test() throws Throwable {
			return (State) super.test();
		}

		/**
		 * 具体类型:<br>
		 * Function&lt;State, ? extends E&gt; <br>
		 * &lt;E extends Throwable&gt;
		 * @author Frodez
		 */
		@Override
		@SuppressWarnings("unchecked")
		public State test(Function function) throws Throwable {
			return (State) super.test(function);
		}

		/**
		 * 具体类型:<br>
		 * Function&lt;String, ? extends E&gt; <br>
		 * &lt;E extends Throwable&gt;
		 * @author Frodez
		 */
		@Override
		@SuppressWarnings("unchecked")
		public State testWithMsg(Function function) throws Throwable {
			return (State) super.testWithMsg(function);
		}

		/**
		 * state专用异常
		 * @author Frodez
		 */
		private static class StateException extends RuntimeException {

			private static final long serialVersionUID = 1L;

			public StateException(String message) {
				super(message);
			}

		}

	}

	/**
	 * 包含状态消息和数据的获取值
	 * @author Frodez
	 */
	@ToString
	@EqualsAndHashCode
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	public static class Value<V> implements Serializable {

		private static final long serialVersionUID = 1L;

		/**
		 * 默认json缓存(数组类型),只有默认实例才存在
		 */
		@Nullable
		@JsonIgnore
		@ToString.Exclude
		@EqualsAndHashCode.Exclude
		private transient byte[] cache;

		/**
		 * 状态
		 */
		@NotNull
		@Getter
		@ApiModelProperty("状态")
		private int code;

		/**
		 * 消息
		 */
		@NotNull
		@Getter
		@ApiModelProperty("消息")
		private String message;

		/**
		 * 数据
		 */
		@ApiModelProperty("数据")
		private V data;

		/**
		 * 判断是否可用,true:可用 false:不可用<br>
		 * @author Frodez
		 */
		public boolean able() {
			return code == ResultEnum.SUCCESS.val;
		}

		/**
		 * 判断是否可用,true:不可用 false:可用<br>
		 * @author Frodez
		 */
		public boolean unable() {
			return code != ResultEnum.SUCCESS.val;
		}

		/**
		 * 判断是否可用,如果不可用,则抛出Exception
		 * @author Frodez
		 */
		public <E extends Throwable> Value<V> test() throws E {
			if (code != ResultEnum.SUCCESS.val) {
				throw new IllegalStateException(errorMsg());
			}
			return this;
		}

		/**
		 * 判断是否可用,如果不可用,则抛出Exception
		 * @author Frodez
		 */
		public <E extends Throwable> Value<V> test(Function<Value<V>, ? extends E> function) throws E {
			if (code != ResultEnum.SUCCESS.val) {
				throw function.apply(this);
			}
			return this;
		}

		/**
		 * 判断是否可用,如果不可用,则抛出Exception
		 * @author Frodez
		 */
		public <E extends Throwable> Value<V> testWithMsg(Function<String, ? extends E> function) throws E {
			if (code != ResultEnum.SUCCESS.val) {
				throw function.apply(this.message);
			}
			return this;
		}

		/**
		 * 获取数据,如果为空则抛出NoSuchElementException
		 * @author Frodez
		 */
		public V get() {
			if (data == null) {
				throw new NoSuchElementException("No data present");
			}
			return data;
		}

		/**
		 * 获取数据,如果为空则返回other
		 * @author Frodez
		 */
		public V or(V other) {
			return data != null ? data : other;
		}

		/**
		 * 判断值是否存在,为空则抛出NoSuchElementException
		 * @author Frodez
		 */
		public Value<V> present() {
			if (data == null) {
				throw new NoSuchElementException("No data present");
			}
			return this;
		}

		/**
		 * 转换成Optional
		 * @author Frodez
		 */
		public Optional<V> optional() {
			return data != null ? Optional.of(data) : Optional.empty();
		}

		/**
		 * 使用异步包装
		 * @author Frodez
		 */
		public ListenableFuture<Value<V>> async() {
			return new AsyncResult<>(this);
		}

		/**
		 * 默认错误信息
		 * @author Frodez
		 */
		String errorMsg() {
			return UString.concat("code: ", String.valueOf(code), ", message: ", message);
		}

		/**
		 * 获取json字符串数组缓存(数组形式)<br>
		 * 仅当为默认示例时使用.其他时候为空.<br>
		 * <strong>禁止对cache做任何修改!!!</strong>
		 * @author Frodez
		 */
		byte[] cache() {
			return cache;
		}

		/**
		 * 获取json字符串
		 * @author Frodez
		 */
		@SneakyThrows
		public String json() {
			return cache != null ? new String(cache) : ResultHelper.WRITER.writeValueAsString(this);
		}

		/**
		 * 获取对应的HttpStatus
		 * @author Frodez
		 */
		public HttpStatus httpStatus() {
			return ResultEnum.of(code).status;
		}

		/**
		 * 获取对应的状态枚举
		 * @author Frodez
		 */
		public ResultEnum resultEnum() {
			return ResultEnum.of(code);
		}

		private Value() {

		}

		private Value(V data) {
			this.data = data;
			this.code = ResultEnum.SUCCESS.val;
			this.message = ResultEnum.SUCCESS.desc;
		}

		private Value(ResultEnum status) {
			this.data = null;
			this.code = status.val;
			this.message = status.desc;
		}

		private Value(String message, ResultEnum status) {
			this.data = null;
			this.code = status.val;
			this.message = message;
		}

	}

	/**
	 * 成功状态(有数据)
	 * @author Frodez
	 */
	public static <V> Value<V> success(V data) {
		Assert.notNull(data, "data must not be null");
		return new Value<>(data);
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * @author Frodez
	 */
	public static <V> Value<PageData<V>> page(int pageNum, int pageSize, long total, Collection<V> data) {
		Assert.notNull(data, "data must not be null");
		return new Value<>(new PageData<>(pageNum, pageSize, total, data));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * @author Frodez
	 */
	public static <V> Value<PageData<V>> page(Page<V> page) {
		return new Value<>(new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getResult()));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * 在可能会对数据进行二次处理,导致类型变化时使用<br>
	 * @author Frodez
	 */
	public static <V> Value<PageData<V>> page(Page<?> page, Collection<V> data) {
		Assert.notNull(data, "data must not be null");
		return new Value<>(new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(), data));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * @author Frodez
	 */
	public static <V> Value<PageData<V>> page(PageInfo<V> page) {
		return new Value<>(new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getList()));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * 在可能会对数据进行二次处理,导致类型变化时使用<br>
	 * @author Frodez
	 */
	public static <V> Value<PageData<V>> page(PageInfo<?> page, Collection<V> data) {
		Assert.notNull(data, "data must not be null");
		return new Value<>(new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(), data));
	}

	/**
	 * 根据情况判断成功或者失败
	 * @author Frodez
	 */
	public static State state(boolean state) {
		return state ? ResultHelper.STATE_CACHE.get(ResultEnum.SUCCESS) : ResultHelper.STATE_CACHE.get(ResultEnum.FAIL);
	}

	/**
	 * 成功状态
	 * @author Frodez
	 */
	public static State success() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.SUCCESS);
	}

	/**
	 * 失败状态(无信息)
	 * @author Frodez
	 */
	public static State fail() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.FAIL);
	}

	/**
	 * 失败状态(有信息)
	 * @author Frodez
	 */
	public static State fail(String message) {
		Assert.notNull(message, "message must not be null");
		return new State(message, ResultEnum.FAIL);
	}

	/**
	 * 请求参数错误状态(无信息)
	 * @author Frodez
	 */
	public static State errorRequest() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.ERROR_PARAMETER);
	}

	/**
	 * 请求参数错误状态(有信息)
	 * @author Frodez
	 */
	public static State errorRequest(String message) {
		Assert.notNull(message, "message must not be null");
		return new State(message, ResultEnum.ERROR_PARAMETER);
	}

	/**
	 * 服务器错误状态(无信息)
	 * @author Frodez
	 */
	public static State errorService() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.ERROR_SERVICE);
	}

	/**
	 * 服务器错误状态(有信息)
	 * @author Frodez
	 */
	public static State errorService(String message) {
		Assert.notNull(message, "message must not be null");
		return new State(message, ResultEnum.ERROR_SERVICE);
	}

	/**
	 * 服务器错误状态(有信息)
	 * @author Frodez
	 */
	public static State errorService(Throwable message) {
		Assert.notNull(message, "message must not be null");
		return new State(message.getMessage(), ResultEnum.ERROR_SERVICE);
	}

	/**
	 * 未登录状态
	 * @author Frodez
	 */
	public static State notLogin() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.NOT_LOGIN);
	}

	/**
	 * 重复登录状态
	 * @author Frodez
	 */
	public static State duplicatedLogin() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.DULPICATED_LOGIN);
	}

	/**
	 * 已过期状态
	 * @author Frodez
	 */
	public static State expired() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.EXPIRED);
	}

	/**
	 * 未通过验证状态
	 * @author Frodez
	 */
	public static State noAuth() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.NO_AUTH);
	}

	/**
	 * 缺少操作权限状态
	 * @author Frodez
	 */
	public static State noAccess() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.NO_ACCESS);
	}

	/**
	 * 重复请求状态
	 * @author Frodez
	 */
	public static State repeatRequest() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.REPEAT_REQUEST);
	}

	/**
	 * 服务器繁忙状态
	 * @author Frodez
	 */
	public static State busy() {
		return ResultHelper.STATE_CACHE.get(ResultEnum.BUSY);
	}

	/**
	 * jackson writer
	 * @author Frodez
	 */
	public static ObjectWriter writer() {
		return ResultHelper.WRITER;
	}

	/**
	 * jackson reader
	 * @author Frodez
	 */
	public static ObjectReader reader() {
		return ResultHelper.READER;
	}

	/**
	 * 状态枚举
	 * @author Frodez
	 */
	@AllArgsConstructor
	public enum ResultEnum {

		/**
		 * 操作成功,与预期相符
		 */
		SUCCESS(1000, HttpStatus.OK, "成功"),
		/**
		 * 操作失败,与预期不符
		 */
		FAIL(1001, HttpStatus.OK, "失败"),
		/**
		 * 请求参数错误
		 */
		ERROR_PARAMETER(1002, HttpStatus.BAD_REQUEST, "请求参数错误"),
		/**
		 * 服务器错误
		 */
		ERROR_SERVICE(1003, HttpStatus.INTERNAL_SERVER_ERROR, "服务器错误"),
		/**
		 * 未登录
		 */
		NOT_LOGIN(2001, HttpStatus.UNAUTHORIZED, "未登录"),
		/**
		 * 重复登录
		 */
		DULPICATED_LOGIN(2002, HttpStatus.UNAUTHORIZED, "重复登录"),
		/**
		 * 已过期
		 */
		EXPIRED(2003, HttpStatus.UNAUTHORIZED, "已过期"),
		/**
		 * 未通过验证
		 */
		NO_AUTH(2004, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "未通过验证"),
		/**
		 * 缺少操作权限
		 */
		NO_ACCESS(2005, HttpStatus.FORBIDDEN, "无权限"),
		/**
		 * 重复请求
		 */
		REPEAT_REQUEST(2006, HttpStatus.LOCKED, "重复请求"),
		/**
		 * 服务器繁忙
		 */
		BUSY(2007, HttpStatus.SERVICE_UNAVAILABLE, "服务器繁忙");

		/**
		 * 自定义状态码
		 */
		public final int val;

		/**
		 * http状态码
		 */
		public final HttpStatus status;

		/**
		 * 描述
		 */
		public final String desc;

		private static final Map<Integer, ResultEnum> enumMap;

		static {
			var builder = ImmutableMap.<Integer, ResultEnum>builder();
			for (ResultEnum iter : ResultEnum.values()) {
				builder.put(iter.val, iter);
			}
			enumMap = builder.build();
		}

		public static ResultEnum of(Integer value) {
			return enumMap.get(value);
		}

	}

	/**
	 * Result工具类
	 * @author Frodez
	 */
	@UtilityClass
	@SuppressWarnings("unchecked")
	private static class ResultHelper {

		/**
		 * 默认类型实例
		 */
		static final EnumMap<ResultEnum, State> STATE_CACHE = new EnumMap<>(ResultEnum.class);

		/**
		 * jackson writer
		 */
		static final ObjectWriter WRITER;

		/**
		 * jackson writer
		 */
		static final ObjectReader READER;

		static {
			// ObjectMapper mapper = new ObjectMapper();
			// mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
			// WRITER = mapper.writerFor(Value.class);
			// READER = mapper.readerFor(Value.class);
			WRITER = JSON.mapper().writerFor(Value.class);
			READER = JSON.mapper().readerFor(Value.class);
			for (ResultEnum item : ResultEnum.values()) {
				State state = new State(item);
				try {
					((Value<Object>) state).cache = WRITER.writeValueAsBytes(state);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
				STATE_CACHE.put(item, state);
			}
			Assert.notNull(WRITER, "writer must not be null");
			Assert.notNull(READER, "reader must not be null");
			Assert.notNull(STATE_CACHE, "STATE_CACHE must not be null");
		}

	}

	@SuppressWarnings("unused")
	@SneakyThrows
	public static void main(String[] args) {
		State a = Result.fail().test((item) -> {
			State t = (State) item;
			System.out.println(t.getClass().getCanonicalName());
			return new IllegalArgumentException();
		});
		var c = a.able() ? Result.success(1) : Result.fail();
		Value<Integer> b = Result.success(2);
		System.out.println(a.json());
		System.out.println(b.json());
	}

}
