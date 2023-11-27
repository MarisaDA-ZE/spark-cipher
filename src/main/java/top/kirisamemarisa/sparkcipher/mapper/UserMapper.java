package top.kirisamemarisa.sparkcipher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.kirisamemarisa.sparkcipher.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
