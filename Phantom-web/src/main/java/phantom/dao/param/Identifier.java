package phantom.dao.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户凭证
 * @author Frodez
 */
@Data
@ApiModel(description = "用户凭证")
public class Identifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Length(max = 255)
	@ApiModelProperty("用户名")
	private String username;

	@NotBlank
	@Length(min = 8, max = 25)
	@ApiModelProperty("密码")
	private String password;

}
