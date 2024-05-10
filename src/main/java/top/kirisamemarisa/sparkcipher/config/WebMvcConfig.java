package top.kirisamemarisa.sparkcipher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kirisamemarisa.sparkcipher.interceptor.LoginHandlerInterceptor;

import javax.annotation.Resource;
import java.util.Arrays;


/**
 * @Author Marisa
 * @Description WebMvcConfig.描述
 * @Date 2023/2/7
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 统一前缀
    @Value("${mrs.controller.path-prefix}")
    private String pathPrefix;
    private final String[] AUTH_EXCLUDE_PATH = {
            "/crypto/**",   // 密钥服务
            "/login/accountLogin",      // 用户登录(不能写/login/**，因为登出接口不放行)
            "/login/phoneLogin",        // 用户登录（手机号）
            "/login/accountCreate",     // 用户注册
            "/login/getCodePhone",      // 获取手机号验证码
            "/user/add",    // 添加用户
    };

    @Resource
    private LoginHandlerInterceptor loginHandlerInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns("*") // 使用allowedOriginPatterns代替allowedOrigins
                .allowCredentials(true);
    }

    /**
     * 统一前缀
     *
     * @param configurer 配置对象
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        System.out.println("pathPrefix: " + pathPrefix);
        configurer.addPathPrefix(pathPrefix, c -> c.isAnnotationPresent(RestController.class));
    }

    /**
     * 注册拦截器
     *
     * @param registry 拦截器对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (int i = 0; i < AUTH_EXCLUDE_PATH.length; i++) {
            AUTH_EXCLUDE_PATH[i] = pathPrefix + AUTH_EXCLUDE_PATH[i];
        }

        System.out.println("不用token的接口: " + Arrays.toString(AUTH_EXCLUDE_PATH));
        registry
                .addInterceptor(loginHandlerInterceptor)
                .excludePathPatterns(AUTH_EXCLUDE_PATH);
    }
}
