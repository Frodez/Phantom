package phantom.dao.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import phantom.aop.validation.annotation.Mapping;
import phantom.constant.AvailableStatus;

/**
 * @description 评论表
 * @table tb_comment
 */
@Data
@Entity
@Table(name = "tb_comment")
@ApiModel(description = "评论信息")
public class Comment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID(不能为空)
	 */
	@Id
	@NotNull
	@Column(name = "id")
	@ApiModelProperty("ID")
	private Long id;

	/**
	 * 用户ID(0为匿名)(不能为空)
	 */
	@NotNull
	@Column(name = "user_id")
	@ApiModelProperty("用户ID(0为匿名)")
	private Long userId;

	/**
	 * 文章ID(不能为空)
	 */
	@NotNull
	@Column(name = "article_id")
	@ApiModelProperty("文章ID")
	private Long articleId;

	/**
	 * 创建时间(不能为空)
	 */
	@NotNull
	@Column(name = "create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 状态 1:可用 2:不可用(不能为空,默认值:1)
	 */
	@NotNull
	@Column(name = "status")
	@Mapping(AvailableStatus.class)
	private Byte status = 1;

	/**
	 * 喜欢(不能为空,默认值:0)
	 */
	@NotNull
	@Column(name = "likes")
	@ApiModelProperty("喜欢")
	private Long likes = 0L;

	/**
	 * 不喜欢(不能为空,默认值:0)
	 */
	@NotNull
	@Column(name = "dislikes")
	@ApiModelProperty("不喜欢")
	private Long dislikes = 0L;

	/**
	 * 评论内容(不能为空)
	 */
	@NotBlank
	@Length(max = 65535)
	@Column(name = "content", length = 65535)
	@ApiModelProperty("评论内容")
	private String content;
}
