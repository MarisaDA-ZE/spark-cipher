package top.kirisamemarisa.sparkcipher.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.kirisamemarisa.sparkcipher.entity.User;

import javax.annotation.Resource;

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
        String uid = TokenUtils.decryptToken(tk, "account");
        if (StringUtils.isBlank(uid)) return null;
        Object o = redisTemplate.opsForValue().get(uid);
        if (ObjectUtils.isEmpty(o)) return null;
        return (User) o;
    }
}
