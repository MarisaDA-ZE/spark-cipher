package top.kirisamemarisa.sparkcipher.interceptor;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.util.MD5Utils;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Author Marisa
 * @Description 登录拦截器
 * @Date 2023/2/7
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

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
        String token = request.getHeader("Authorization");


        // 请求头未携带token
        if (StringUtils.isBlank(token) || StringUtils.isBlank(token.replaceAll("Bearer ", ""))) {
            throw new UnauthorizedException("token不存在！");
        }
        token = token.replaceAll("Bearer ", "");
        String account = TokenUtils.decryptToken(token, "account");

        // token校验失败(已过期)或者取不出账号名
        // System.out.println("账号: " + account + ", 校验: " + JwtUtil.verify(token));
        if (!TokenUtils.verify(token) || StringUtils.isBlank(account)) {
            throw new UnauthorizedException("token已过期");
        }

        // 检查token是否已被拉黑
        String md5Tk = MD5Utils.md5(token);
        String loggedUserInfo = redisTemplate.opsForValue().get(md5Tk);

        // 登录信息不为空 将之前的用户挤下线
        // 检查是否用户登录
        // 校验登录信息 ip地址和操作设备必须和登录时保持一致
        // token与记录的不一致

        return true;
    }
}

