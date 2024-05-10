package top.kirisamemarisa.sparkcipher.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.kirisamemarisa.sparkcipher.entity.dto.CertLoginDto;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.ICertificationService;

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
    private ICertificationService certificationService;


    /**
     * 在Controller之前调用
     *
     * @param request  请求
     * @param response 响应
     * @param handler  .
     * @return 是否拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        System.out.println(method + ", " + request.getHeader("HOST") + request.getRequestURI());
        // 放行所有options请求
        if ("OPTIONS".equals(request.getMethod())) return true;

        String rawToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(rawToken)) {
            throw new UnauthorizedException("token不存在！");
        }

        String token = rawToken.replaceFirst("Bearer ", "");
        if (StringUtils.isBlank(token)) {
            throw new UnauthorizedException("无效的token格式");
        }

        // 到这里前端的token是符合规范的，下一步将token交给认证中心
        CertLoginDto loginStatus = certificationService.verifyTokenInfo(token);
        if (!loginStatus.isStatus()) {
            throw new UnauthorizedException(loginStatus.getMsg());
        }
        return true;
    }
}

