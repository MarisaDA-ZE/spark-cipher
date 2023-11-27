package top.kirisamemarisa.sparkcipher.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.mapper.UserMapper;
import top.kirisamemarisa.sparkcipher.service.IUserService;

/**
 * @Author Marisa
 * @Description 用户接口实现类
 * @Date 2023/11/27
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
