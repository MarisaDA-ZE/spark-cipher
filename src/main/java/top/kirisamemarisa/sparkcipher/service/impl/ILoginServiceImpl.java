package top.kirisamemarisa.sparkcipher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.vo.LoginVo;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.entity.vo.UserVo;
import top.kirisamemarisa.sparkcipher.entity.resp.MrsLResp;
import top.kirisamemarisa.sparkcipher.exception.NotFoundException;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.ILoginService;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.SaltGenerator;
import top.kirisamemarisa.sparkcipher.util.SnowflakeUtils;
import top.kirisamemarisa.sparkcipher.util.encrypto.md5.MD5Utils;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    public String accountCreate(LoginVo loginVo) {
        String account = loginVo.getAccount();
        String password = loginVo.getPassword();
        String phoneNo = loginVo.getPhoneNo();

        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) return "请填写用户名密码";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", account);
        if (StringUtils.isNotBlank(phoneNo)) queryWrapper.eq("PHONE", phoneNo);
        User dbUser = userService.getOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(dbUser)) return "当前账户已存在";

        User savedUser = new User();
        boolean b1 = savedUser.verifyUserName(account);
        boolean b2 = savedUser.verifyPassword(password);
        if (!b1 || !b2 || !savedUser.verifyNullable()) return "校验未通过,请检查参数!";

        // 创建账户
        String snowflakeId = SnowflakeUtils.nextId();
        String salt = SaltGenerator.generateSalt();
        password = MD5Utils.md5(password + salt);
        savedUser.setId(snowflakeId);
        savedUser.setLevel(1);
        savedUser.setPassword(password);
        savedUser.setSalt(salt);
        savedUser.setCreateTime(new Date());
        savedUser.setCreateBy(snowflakeId);
        boolean save = userService.save(savedUser);
        return save ? null : "用户创建失败，请联系管理员";
    }

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
