package top.kirisamemarisa.sparkcipher.interceptor;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static top.kirisamemarisa.sparkcipher.common.Constants.TOKEN_EXPIRE_TIME;


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
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        // 放行所有options请求
        if ("OPTIONS".equals(request.getMethod())) return true;
        String token = request.getHeader("Authorization");
        // 请求头未携带token
        if (StringUtils.isBlank(token) || StringUtils.isBlank(token.replaceAll("Bearer ", ""))) {
            throw new UnauthorizedException("token不存在！");
        }
        token = token.replaceAll("Bearer ", "");
        // 用户ID
        String uid = TokenUtils.decryptToken(token, "account");

        // token校验失败(已过期)或者取不出账号名
        // System.out.println("账号: " + account + ", 校验: " + JwtUtil.verify(token));
        if (!TokenUtils.verify(token) || StringUtils.isBlank(uid)) {
            throw new UnauthorizedException("token已过期！");
        }
        // token 拉黑操作(还没做)


        String loggedTk = (String) redisTemplate.opsForValue().get(uid + ".token");
        SecurityUtils.stk.set(token);

        // 登录信息不为空 将之前的用户挤下线
        // 检查是否用户登录
        // 校验登录信息 ip地址和操作设备必须和登录时保持一致
        // token与记录的不一致

        // 最后如果上述所有校验都没有问题，则刷新redis中该用户的过期时间
        User dbUser = userService.getById(uid);
        redisTemplate.opsForValue().set(uid, dbUser, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        // 更新token
        redisTemplate.opsForValue().set(uid + ".token", token, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        return true;
    }
}

