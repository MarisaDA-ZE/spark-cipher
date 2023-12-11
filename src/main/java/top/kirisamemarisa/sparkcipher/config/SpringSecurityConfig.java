package top.kirisamemarisa.sparkcipher.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author Marisa
 * @Description SpringSecurityConfig.描述
 * @Date 2023/12/11
 */
public class SpringSecurityConfig // extends WebSecurityConfigurerAdapter
{
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // super.configure(http);
//        http    // 关闭csrf（跨站请求伪造）
//                .csrf().disable()
//                //不通过Session获取SecurityContext
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // 放行部分接口
//                .antMatchers("/auth/accountLogin", "/auth/createUser").anonymous()
//                // 除上面外的所有请求全部需要鉴权认证
//                .anyRequest().authenticated();
//    }
//
//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }
}
