package top.kirisamemarisa.sparkcipher.interceptor;

//import com.sun.istack.internal.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.kirisamemarisa.sparkcipher.entity.enums.JwtKeys;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static top.kirisamemarisa.sparkcipher.common.Constants.*;


/**
 * @Author Marisa
 * @Description 登录拦截器
 * @Date 2023/2/7
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Resource
    private IUserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 在Controller之前调用
     *
     * @param request  请求
     * @param response 响应
     * @param handler  .
     * @return 是否拦截
     */
    @Override
//    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        System.out.println("请求方式: " + request.getMethod());
        System.out.println("请求地址: " + request.getHeader("HOST") + request.getRequestURI());
        // 放行所有options请求
        if ("OPTIONS".equals(request.getMethod())) return true;
        String token = request.getHeader("Authorization");
        // 请求头未携带token
        if (StringUtils.isBlank(token) || StringUtils.isBlank(token.replaceAll("Bearer ", ""))) {
            throw new UnauthorizedException("token不存在！");
        }
        token = token.replaceAll("Bearer ", "");
        // 用户ID
        String uid = TokenUtils.decryptToken(token, JwtKeys.UID.getKey());
        String loginToken = (String) redisTemplate.opsForValue().get(uid + TOKEN_SUFFIX);

        // token校验失败(已过期)或者取不出ID 或 Redis中这个Token不正确
        if (!TokenUtils.verify(token) || StringUtils.isBlank(uid) || !TokenUtils.verify(loginToken)) {
            throw new UnauthorizedException("token已过期！");
        }

        // token 拉黑操作(还没做)
        SecurityUtils.stk.set(token);

        // 登录信息不为空 将之前的用户挤下线
        // 检查是否用户登录
        // 校验登录信息 ip地址和操作设备必须和登录时保持一致
        // token与记录的不一致

        // 最后如果上述所有校验都没有问题，则 刷新 redis中该用户的过期时间
        User dbUser = userService.getById(uid);
        redisTemplate.opsForValue().set(uid + USER_SUFFIX, dbUser, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        // 更新token
        redisTemplate.opsForValue().set(uid + TOKEN_SUFFIX, token, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        return true;
    }
}

