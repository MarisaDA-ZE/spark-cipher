package top.kirisamemarisa.sparkcipher.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.entity.dto.CertLoginDto;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.entity.enums.JwtKeys;
import top.kirisamemarisa.sparkcipher.service.ICertificationService;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static top.kirisamemarisa.sparkcipher.common.Constants.*;

/**
 * @Author Marisa
 * @Description ICertificationServiceImpl.描述
 * @Date 2024/5/10
 */
@Service
public class ICertificationServiceImpl implements ICertificationService {
    @Resource
    private IUserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public CertLoginDto verifyTokenInfo(String token) {
        // token 校验失败
        if (!TokenUtils.verify(token)) return new CertLoginDto(false, "token已过期");
        // 用户ID
        String uid = TokenUtils.decryptToken(token, JwtKeys.UID.getKey());
        String loggedToken = (String) redisTemplate.opsForValue().get(uid + TOKEN_SUFFIX);

        // redis中不存在此token
        if (!TokenUtils.verify(loggedToken)) return new CertLoginDto(false, "token已过期");


        // 将token保存到线程变量中
        SecurityUtils.stk.set(token);

        // 登录信息不为空 将之前的用户挤下线
        // 检查是否用户登录
        // 校验登录信息 ip地址和操作设备必须和登录时保持一致
        // token与记录的不一致

        // 最后如果上述所有校验都没有问题，则 刷新 redis中该用户的过期时间
        Object u = redisTemplate.opsForValue().get(uid + USER_SUFFIX);
        User dbUser;
        if (ObjectUtils.isEmpty(u)) {
            dbUser = userService.getById(uid);
        } else {
            dbUser = (User) u;
        }
        // 更新token和用户的过期时间
        redisTemplate.opsForValue().set(uid + USER_SUFFIX, dbUser, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(uid + TOKEN_SUFFIX, token, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        return new CertLoginDto(true, "");
    }
}
