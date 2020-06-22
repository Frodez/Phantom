package phantom.dao.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 注册信息参数
 * @author Frodez
 */
@Data
@ApiModel(description = "注册信息参数")
public class Register implements Serializable {

	private static final long serialVersionUID = 1L;

	@Valid
	@NotNull
	private Identifier identifier;

	/**
	 * 邮箱
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("邮箱")
	private String email;

}
