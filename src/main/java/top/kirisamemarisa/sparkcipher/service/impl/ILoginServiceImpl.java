package top.kirisamemarisa.sparkcipher.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.entity.LoginUser;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.exception.NotFoundException;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.ILoginService;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.MD5Utils;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static top.kirisamemarisa.sparkcipher.common.Constants.TOKEN_EXPIRE_TIME;

/**
 * @Author Marisa
 * @Description ILoginServiceImpl.描述
 * @Date 2023/11/30
 */
@Service
public class ILoginServiceImpl implements ILoginService {


    @Resource
    private IUserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public String loginByAccount(LoginUser loginUser, HttpServletRequest req) {
        System.out.println(loginUser);
        String account = loginUser.getAccount();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", account);
        User dbUser = userService.getOne(queryWrapper);
        System.out.println(dbUser);
        if (dbUser == null) throw new NotFoundException("用户不存在！");
        String p = loginUser.getPassword();
        String pwd = MD5Utils.md5(p + dbUser.getSalt());
        if (!dbUser.getPassword().equals(pwd)) throw new UnauthorizedException("用户名密码错误！");
        System.out.println("登录成功！");
        String uid = dbUser.getId();
        long now = System.currentTimeMillis();
        String token = TokenUtils.sign(uid, loginUser.getLoginMac(), String.valueOf(now));
        String dbJson = JSONObject.toJSONString(dbUser);
        System.out.println(dbJson);
        // 保存用户
        redisTemplate.opsForValue().set(uid, dbUser, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        // 保存token
        redisTemplate.opsForValue().set(uid + ".token", token, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        return token;
    }
}
