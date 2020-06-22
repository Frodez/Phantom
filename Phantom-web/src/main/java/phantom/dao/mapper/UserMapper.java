package phantom.dao.mapper;

import org.springframework.stereotype.Repository;
import phantom.dao.model.User;
import phantom.mybatis.mapper.DataMapper;

/**
 * @description 用户表
 * @table tb_user
 */
@Repository
public interface UserMapper extends DataMapper<User> {
}