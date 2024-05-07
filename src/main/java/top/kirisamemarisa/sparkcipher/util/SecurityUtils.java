package top.kirisamemarisa.sparkcipher.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.kirisamemarisa.sparkcipher.entity.enums.JwtKeys;
import top.kirisamemarisa.sparkcipher.entity.User;

import javax.annotation.Resource;

import static top.kirisamemarisa.sparkcipher.common.Constants.USER_SUFFIX;

/**
 * @Author Marisa
 * @Description SecurityUtils.描述
 * @Date 2023/12/5
 */
@Component
public class SecurityUtils {
    public static ThreadLocal<String> stk = new ThreadLocal<>();
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取当前的权限用户
     *
     * @return .
     */
    public User getAuthUser() {
        String tk = stk.get();
        System.out.println("thread token: " + tk);
        String uid = TokenUtils.decryptToken(tk, JwtKeys.UID.getKey());
        Object u = redisTemplate.opsForValue().get(uid + USER_SUFFIX);
        if (StringUtils.isBlank(uid) || ObjectUtils.isEmpty(u)) return null;
        return (User) u;
    }
}
