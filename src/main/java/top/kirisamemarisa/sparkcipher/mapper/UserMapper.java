package top.kirisamemarisa.sparkcipher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.kirisamemarisa.sparkcipher.entity.User;

/**
 * @Author Marisa
 * @Description 用户服务mapper
 * @Date 2023/11/27
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
