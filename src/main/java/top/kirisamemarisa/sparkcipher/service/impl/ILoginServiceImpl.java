package top.kirisamemarisa.sparkcipher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.entity.vo.LoginVo;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.entity.vo.UserVo;
import top.kirisamemarisa.sparkcipher.entity.resp.MrsLResp;
import top.kirisamemarisa.sparkcipher.exception.NotFoundException;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.ILoginService;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.encrypto.md5.MD5Utils;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static top.kirisamemarisa.sparkcipher.common.Constants.*;

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
    public MrsLResp loginByAccount(LoginVo loginVo, HttpServletRequest req) {
        System.out.println(loginVo);
        String account = loginVo.getAccount();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", account);
        User dbUser = userService.getOne(queryWrapper);
        System.out.println(dbUser);
        if (dbUser == null) throw new NotFoundException("用户不存在！");
        String p = loginVo.getPassword();
        String pwd = MD5Utils.md5(p + dbUser.getSalt());
        if (!dbUser.getPassword().equals(pwd)) throw new UnauthorizedException("用户名密码错误！");
        System.out.println("登录成功！");
        String uid = dbUser.getId();
        long now = System.currentTimeMillis();
        String token = TokenUtils.sign(uid, String.valueOf(now));
        // 保存用户
        redisTemplate.opsForValue().set(uid + USER_SUFFIX, dbUser, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        // 保存token
        redisTemplate.opsForValue().set(uid + TOKEN_SUFFIX, token, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(dbUser, userVo);
        return new MrsLResp(userVo, token);
    }
}
