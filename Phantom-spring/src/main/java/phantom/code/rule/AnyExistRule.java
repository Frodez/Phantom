package phantom.code.rule;

import java.lang.reflect.Field;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.SneakyThrows;
import phantom.code.checker.CodeCheckException;
import phantom.common.UEmpty;
import phantom.tool.validate.annotation.AnyExist;

/**
 * 检查@AnyExist注解
 * @see phantom.tool.validate.annotation.AnyExist
 * @author Frodez
 */
public class AnyExistRule implements Rule {

	private boolean hasCheck(Field field) {
		return field.isAnnotationPresent(NotNull.class) || field.isAnnotationPresent(NotEmpty.class)
				|| field.isAnnotationPresent(NotBlank.class);
	}

	@Override
	@SneakyThrows
	public void check(Class<?> klass) throws CodeCheckException {
		AnyExist annotation = klass.getAnnotation(AnyExist.class);
		if (annotation != null) {
			String[] fields = annotation.value();
			if (UEmpty.no(fields)) {
				for (String string : fields) {
					Field field = klass.getDeclaredField(string);
					if (hasCheck(field)) {
						throw new CodeCheckException(klass.getCanonicalName(), "的", string, "字段存在非空检查,不需要添加",
								Util.classText(AnyExist.class));
					}
					if (field == null) {
						throw new CodeCheckException(klass.getCanonicalName(), "没有", string, "字段");
					}
				}
			}
		}
	}

}
