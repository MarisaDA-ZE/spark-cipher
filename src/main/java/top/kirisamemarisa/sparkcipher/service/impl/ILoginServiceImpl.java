package top.kirisamemarisa.sparkcipher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.annotations.UniqueField;
import top.kirisamemarisa.sparkcipher.entity.dto.SendCodeDto;
import top.kirisamemarisa.sparkcipher.entity.vo.LoginVo;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.entity.vo.SendCodeVo;
import top.kirisamemarisa.sparkcipher.entity.vo.UserVo;
import top.kirisamemarisa.sparkcipher.entity.resp.MrsLResp;
import top.kirisamemarisa.sparkcipher.exception.CodeExpiredException;
import top.kirisamemarisa.sparkcipher.exception.InternalServerErrorException;
import top.kirisamemarisa.sparkcipher.exception.NotFoundException;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.ILoginService;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.*;
import top.kirisamemarisa.sparkcipher.util.encrypto.md5.MD5Utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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

    @Resource
    private MrsEmailUtil emailUtil;


    @Override
    public String accountCreate(LoginVo loginVo) {
        String account = loginVo.getAccount();
        String password = loginVo.getPassword();
        String phoneNo = loginVo.getPhoneNo();
        String email = loginVo.getEmail();

        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) return "请填写用户名密码";
        if (StringUtils.isBlank(phoneNo) && StringUtils.isBlank(email)) return "请填写手机号或邮箱";

        // 填了手机号就要校验 手机验证码
        if(StringUtils.isNotBlank(phoneNo)){
            Object pop = redisTemplate.opsForValue().get(phoneNo + PHONE_VERIFY_SUFFIX);
            if(pop == null) return "手机未校验";
            try {
                SendCodeDto codeDto = (SendCodeDto) pop;
                long now = System.currentTimeMillis();
                long prevSend = codeDto.getLastSendTime();
                if(now - prevSend > PHONE_CODE_EXPIRE_TIME){
                    return "验证码已过期，请重新发送";
                }
                String rCode = codeDto.getCode();
                if(!(rCode+ "").equals(loginVo.getPhoneCode())){
                    return "手机验证码错误";
                }else {
                    // 验证码正确，更新掉使其不能使用
                    codeDto.setCode(rCode + USED_SUFFIX);
                    redisTemplate.opsForValue().set(phoneNo + PHONE_VERIFY_SUFFIX,
                            codeDto, ONE_DAY_SECONDS, TimeUnit.SECONDS);
                }

            }catch (Exception e){
                return "服务器内部错误，请联系管理员";
            }
        }

        // 填了邮箱就要校验 邮箱验证码
        if(StringUtils.isNotBlank(email)){
            Object pop = redisTemplate.opsForValue().get(email + EMAIL_VERIFY_SUFFIX);
            if(pop == null) return "邮箱未校验";
            try {
                SendCodeDto codeDto = (SendCodeDto) pop;
                long now = System.currentTimeMillis();
                long prevSend = codeDto.getLastSendTime();
                if(now - prevSend > EMAIL_CODE_EXPIRE_TIME){
                    return "验证码已过期，请重新发送";
                }
                String rCode = codeDto.getCode();
                boolean isUsed = codeDto.isUsed();  // true: 已使用, false: 未使用

                // 不区分大小写，redis中的code本身就是全大写
                if(!(rCode+ "").equals((loginVo.getEmailCode() + "").toUpperCase()) && !isUsed){
                    return "邮箱验证码错误";
                }else {
                    // 验证码正确，更新使用状态
                    codeDto.setUsed(true);
                    redisTemplate.opsForValue().set(email + EMAIL_VERIFY_SUFFIX,
                            codeDto, ONE_DAY_SECONDS, TimeUnit.SECONDS);
                }
            }catch (Exception e){
                return "服务器内部错误，请联系管理员";
            }
        }

        User queryUser = new User();
        queryUser.setAccount(account);
        queryUser.setPhone(phoneNo);
        queryUser.setEmail(email);
        int keyCount = this.getCountByKey(queryUser);
        if (keyCount > 0) return "账户已存在";

        User savedUser = new User();
        savedUser.setAccount(account);
        savedUser.setNickName(loginVo.getNickName());
        if (StringUtils.isNotBlank(phoneNo)) {
            savedUser.setPhone(phoneNo);
            savedUser.setOriginalPhone(phoneNo);
        }
        if (StringUtils.isNotBlank(email)) {
            savedUser.setEmail(email);
            savedUser.setOriginalEmail(email);
        }

        boolean b1 = savedUser.verifyAccount(account);
        boolean b2 = savedUser.verifyPassword(password);
        if (!b1 || !b2 || !savedUser.verifyNullable()) return "校验未通过,请检查参数!";

        // 创建账户
        String snowflakeId = SnowflakeUtils.nextId();
        String salt = SaltGenerator.generateSalt();
        password = MD5Utils.md5(password + salt);
        savedUser.setId(snowflakeId);
        savedUser.setNickName(savedUser.getAccount());
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
        queryWrapper.eq("ACCOUNT", account);
        User dbUser = userService.getOne(queryWrapper);
        System.out.println(dbUser);
        if (dbUser == null) throw new NotFoundException("用户不存在！");
        String p = loginVo.getPassword();
        String pwd = MD5Utils.md5(p + dbUser.getSalt());
        if (!dbUser.getPassword().equals(pwd)) throw new UnauthorizedException("用户名密码错误！");
        System.out.println("用户名密码登录成功...");
        return loginSuccess(dbUser);
    }

    @Override
    public MrsLResp loginByPhone(LoginVo loginVo, HttpServletRequest req) {
        System.out.println(loginVo);
        String phoneNo = loginVo.getPhoneNo();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PHONE", phoneNo);
        User dbUser = userService.getOne(queryWrapper);
        System.out.println(dbUser);
        if (dbUser == null) throw new NotFoundException("手机号不存在！");
        Object op = redisTemplate.opsForValue().get(phoneNo + PHONE_VERIFY_SUFFIX);
        if (ObjectUtils.isEmpty(op)) throw new CodeExpiredException("验证码已过期！");

        if (!(op instanceof SendCodeDto)) {
            throw new InternalServerErrorException("服务器内部错误，请联系管理员");
        }

        SendCodeDto codeDto = (SendCodeDto) op;
        String rdCode = codeDto.getCode();
        if (rdCode == null || System.currentTimeMillis() - codeDto.getLastSendTime() > PHONE_CODE_EXPIRE_TIME) {
            throw new CodeExpiredException("验证码已过期！");
        }

        boolean used = codeDto.isUsed();
        // 验证码相同则登录成功
        if ((loginVo.getPhoneCode() + "").equals(rdCode) && !used) {
            System.out.println("手机号验证码登录成功...");
            // 作废该验证码
            codeDto.setUsed(true);
            redisTemplate.opsForValue().set(phoneNo + PHONE_VERIFY_SUFFIX, codeDto, ONE_DAY_SECONDS, TimeUnit.SECONDS);
            return loginSuccess(dbUser);
        }
        throw new UnauthorizedException("验证码不正确");
    }

    @Override
    public SendCodeVo sendCodePhone(String phoneNo) {
        if (StringUtils.isBlank(phoneNo)) return SendCodeVo.failed("手机号为空！");
        if (!MrsUtil.verifyPhoneNo(phoneNo)) return SendCodeVo.failed("手机号格式错误！");
        // 手机号已经正确了
        Object op = redisTemplate.opsForValue().get(phoneNo + PHONE_VERIFY_SUFFIX);

        // 之前没有发送过验证码
        if (ObjectUtils.isEmpty(op)) {
            SendCodeDto codeDto = new SendCodeDto();
            String code = MrsUtil.getRandomCodeNum(6);
            codeDto.setAccount(phoneNo);
            codeDto.setCode(code);
            codeDto.setLastSendTime(System.currentTimeMillis());
            codeDto.setRemainingCount(PHONE_CODE_COUNT -1); // 算上本次发送
            try {
                boolean isSend = MrsSMSUtil.sendPhoneCode(phoneNo, code);
                if (!isSend)  return SendCodeVo.failed("发送失败！");
                redisTemplate.opsForValue().set(phoneNo + PHONE_VERIFY_SUFFIX, codeDto,
                        ONE_DAY_SECONDS,
                        TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException e) {
                return SendCodeVo.failed("发送失败！");
            }

            return SendCodeVo.success(phoneNo, code);
        }

        if (!(op instanceof SendCodeDto)) {
            throw new InternalServerErrorException("对象转换失败！");
        }

        // 之前有发送过验证码
        SendCodeDto codeDto = (SendCodeDto) op;
        long now = System.currentTimeMillis();
        long lastSend = codeDto.getLastSendTime();

        // 不一样说明次数不在同一天，次数重置(但要算上本次发送)
        if (isSameDay(now, lastSend)) {
            codeDto.setRemainingCount(PHONE_CODE_COUNT -1);
        }

        if (codeDto.getRemainingCount() <= 0) {
            return SendCodeVo.failed("今日发送次数已用完！");
        }

        if (now - lastSend < 1000 * 60) {
            return SendCodeVo.failed("请勿重复请求！");
        }

        String randomCode = MrsUtil.getRandomCodeNum(6);
        // 不用设置手机号，能拿到对象说明肯定是存在手机号的
        codeDto.setCode(randomCode);
        codeDto.setRemainingCount(codeDto.getRemainingCount() - 1);
        codeDto.setUsed(false);
        codeDto.setLastSendTime(now);
        try {
            boolean isSend = MrsSMSUtil.sendPhoneCode(phoneNo, randomCode);
            if(!isSend)return SendCodeVo.failed("发送失败！");
            redisTemplate.opsForValue().set(phoneNo + PHONE_VERIFY_SUFFIX, codeDto, ONE_DAY_SECONDS, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return SendCodeVo.failed("发送失败！");
        }
        return SendCodeVo.success(phoneNo, randomCode);
    }

    @Override
    public SendCodeVo sendEmailCode(String email) {
        if (StringUtils.isBlank(email)) return SendCodeVo.failed("邮箱号为空！");
        if (!MrsUtil.verifyEmail(email)) return SendCodeVo.failed("邮箱号格式错误！");
        // 邮箱已经正确了
        Object oe = redisTemplate.opsForValue().get(email + EMAIL_VERIFY_SUFFIX);

        // 之前没有发送过验证码
        if (ObjectUtils.isEmpty(oe)) {
            SendCodeDto codeDto = new SendCodeDto();
            String code = MrsUtil.getRandomCodeStr(8);
            codeDto.setAccount(email);
            codeDto.setCode(code);
            codeDto.setLastSendTime(System.currentTimeMillis());
            codeDto.setRemainingCount(EMAIL_CODE_COUNT -1);
            emailUtil.sendCodeEmail(email, code);
            redisTemplate.opsForValue().set(email + EMAIL_VERIFY_SUFFIX,
                    codeDto, ONE_DAY_SECONDS, TimeUnit.SECONDS);
            return SendCodeVo.success(email, code);
        }

        if (!(oe instanceof SendCodeDto)) {
            throw new InternalServerErrorException("对象转换失败！");
        }

        // 之前有发送过验证码
        SendCodeDto codeDto = (SendCodeDto) oe;
        long lastSend = codeDto.getLastSendTime();
        long now = System.currentTimeMillis();

        // 不一样说明次数不在同一天，次数重置
        if (isSameDay(now, lastSend)) {
            codeDto.setRemainingCount(EMAIL_CODE_COUNT -1);
        }

        if (codeDto.getRemainingCount() <= 0) {
            return SendCodeVo.failed("今日发送次数已用完！");
        }

        String randomCode = MrsUtil.getRandomCodeStr(8);
        // 不用设置手机号，能拿到对象说明肯定是存在手机号的
        codeDto.setCode(randomCode);
        codeDto.setRemainingCount(codeDto.getRemainingCount() - 1);
        codeDto.setUsed(false);
        codeDto.setLastSendTime(now);
        emailUtil.sendCodeEmail(email, randomCode);
        redisTemplate.opsForValue().set(email + EMAIL_VERIFY_SUFFIX,
                codeDto, ONE_DAY_SECONDS, TimeUnit.SECONDS);

        return SendCodeVo.success(email, randomCode);
    }

    @Override
    public int getCountByKey(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String, Object> keyMap = getUserKeys(user);
        keyMap.forEach((k, v) -> queryWrapper.eq(k, v).or());
        List<User> list = userService.list(queryWrapper);
        return list.size();
    }

    /**
     * 查看User对象中的唯一键
     *
     * @param user user对象
     * @return 返回值，格式是{key: value}的形式，key是User表中的键，value是前端传的值
     */
    private Map<String, Object> getUserKeys(User user) {
        Map<String, Object> result = new HashMap<>();
        Class<User> clazz = User.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            UniqueField annotation = field.getAnnotation(UniqueField.class);
            if (annotation != null) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(user);
                    if (ObjectUtils.isNotEmpty(value)) {
                        String fieldName = MrsUtil.columnNameTranslate(field.getName(), user);
                        result.put(fieldName, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 账户登录成功后
     *
     * @param user 数据库中查到的合法用户
     * @return 包含token和userVo的响应对象
     */
    private MrsLResp loginSuccess(User user) {
        String uid = user.getId();
        long now = System.currentTimeMillis();
        String token = TokenUtils.sign(uid, String.valueOf(now));
        // 保存用户
        redisTemplate.opsForValue().set(uid + USER_SUFFIX, user, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        // 保存token
        redisTemplate.opsForValue().set(uid + TOKEN_SUFFIX, token, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return new MrsLResp(userVo, token);
    }

    /**
     * 判断两个时间戳是否在同一天
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 是否在同一天（true: 是，false: 否）
     */
    private boolean isSameDay(long time1, long time2) {
        LocalDateTime date1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time1), ZoneId.systemDefault());
        LocalDateTime date2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time2), ZoneId.systemDefault());
        return ChronoUnit.DAYS.between(date1, date2) != 0;
    }

}
