package top.kirisamemarisa.sparkcipher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kirisamemarisa.sparkcipher.interceptor.LoginHandlerInterceptor;

import javax.annotation.Resource;


/**
 * @Author Marisa
 * @Description WebMvcConfig.描述
 * @Date 2023/2/7
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] AUTH_EXCLUDE_PATH = {
            "/auth/accountLogin", // 用户名密码登录
            "/user/add", // 添加用户
    };

    @Resource
    private LoginHandlerInterceptor loginHandlerInterceptor;

    /**
     * 注册拦截器
     *
     * @param registry 拦截器对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(loginHandlerInterceptor)
                .excludePathPatterns(AUTH_EXCLUDE_PATH);
    }
}
