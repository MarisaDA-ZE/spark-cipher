package top.kirisamemarisa.sparkcipher.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Marisa
 * @Description MyBatisPlusConfig.描述
 * @Date 2024/5/7
 */
@Configuration
//地址指向mapper层 持久层也就是Dao接口
@MapperScan("top.kirisamemarisa.sparkcipher.mapper")
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //调用的mybatis的分页拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //我们在此选择数据库的类型，也有其他的参数 我这边选择的mysql
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;


    }

}

