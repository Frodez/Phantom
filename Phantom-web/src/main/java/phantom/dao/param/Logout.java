package phantom.dao.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 登出信息参数
 * @author Frodez
 */
@Data
@ApiModel(description = "登出信息参数")
public class Logout implements Serializable {

	private static final long serialVersionUID = 1L;

	@Valid
	@NotNull
	private Identifier identifier;

	@NotBlank
	@ApiModelProperty("token")
	private String token;

}
