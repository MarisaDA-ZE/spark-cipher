package top.kirisamemarisa.sparkcipher.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.enums.TypeSuffix;
import top.kirisamemarisa.sparkcipher.entity.*;
import top.kirisamemarisa.sparkcipher.entity.resp.MrsLResp;
import top.kirisamemarisa.sparkcipher.entity.vo.LoginVo;
import top.kirisamemarisa.sparkcipher.entity.vo.SendCodeVo;
import top.kirisamemarisa.sparkcipher.service.ILoginService;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import static top.kirisamemarisa.sparkcipher.common.Constants.TOKEN_SUFFIX;

/**
 * @Author Marisa
 * @Description 登录服务
 * @Date 2023/11/30
 */

@RestController
@CrossOrigin
@RequestMapping("/login")
public class LoginController {

    @Resource
    private ILoginService loginService;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 通过账号密码创建账号
     *
     * @param loginVo vo
     * @return 创建结果
     */
    @PutMapping("/accountCreate")
    public MrsResult<?> accountCreate(@RequestBody LoginVo loginVo) {
        String resMsg = loginService.accountCreate(loginVo);
        if (resMsg == null) return MrsResult.ok("创建成功，请返回登录");
        return MrsResult.failed(resMsg);
    }

    /**
     * 向对应手机号发送验证码
     *
     * @param phoneNo 目标手机号
     * @return 发送结果
     */
    @GetMapping("/getCodePhone")
    public MrsResult<?> getCodePhone(@RequestParam(name = "phoneNo") String phoneNo) {
        SendCodeVo sendCodeVo = loginService.sendCodePhone(phoneNo);
        if (!sendCodeVo.isStatus()) return MrsResult.failed(sendCodeVo.getMsg());
        System.out.println("手机号: " + phoneNo + " ,验证码: " + sendCodeVo.getCode());
        return MrsResult.ok(sendCodeVo.getMsg());
    }

    /**
     * 向对应手机号发送验证码
     *
     * @param email 目标手机号
     * @return 发送结果
     */
    @GetMapping("/getCodeEmail")
    public MrsResult<?> getCodeEmail(@RequestParam(name = "email") String email) {
        SendCodeVo sendCodeVo = loginService.sendEmailCode(email);
        if (!sendCodeVo.isStatus()) return MrsResult.failed(sendCodeVo.getMsg());
        System.out.println("邮箱: " + email + " ,验证码: " + sendCodeVo.getCode());
        return MrsResult.ok(sendCodeVo.getMsg());
    }


    /**
     * 通过账号密码的方式登录
     *
     * @param loginVo 前端VO
     * @param req     http请求对象
     * @return .
     */
    @PostMapping("/accountLogin")
    public MrsResult<?> accountLogin(@RequestBody LoginVo loginVo, HttpServletRequest req) {
        System.out.println("登录对象: " + loginVo);
        MrsLResp resp = loginService.loginByAccount(loginVo, req);
        return MrsResult.ok("登录成功！", resp);
    }

    /**
     * 通过手机号验证码的方式登录
     *
     * @param loginVo 前端VO
     * @param req     http请求对象
     * @return .
     */
    @PostMapping("/phoneLogin")
    public MrsResult<?> phoneLogin(@RequestBody LoginVo loginVo, HttpServletRequest req) {
        System.out.println("登录对象: " + loginVo);
        MrsLResp resp = loginService.loginByPhone(loginVo, req);
        return MrsResult.ok("登录成功！", resp);
    }

    /**
     * 查看某个值在库中的数量
     *
     * @param user 键名
     * @return .
     */
    @PostMapping("/getCountByUserKey")
    public MrsResult<?> getCountByUserKey(@RequestBody User user) {
        int keyCount = loginService.getCountByKey(user);
        return MrsResult.ok("查询成功", keyCount);
    }

    /**
     * 账号登出
     *
     * @return .
     */
    @GetMapping("/logout")
    public MrsResult<?> logout() {
        User authUser = securityUtils.getAuthUser();
        System.out.println("退出登录: " + authUser);
        String uid = authUser.getId();
        if (StringUtils.isNotBlank(uid)) {
            redisTemplate.delete(uid);
            redisTemplate.delete(uid + TOKEN_SUFFIX);
            redisTemplate.delete(uid + TypeSuffix.SKP.getTypeSuffix());
            redisTemplate.delete(uid + TypeSuffix.CKP.getTypeSuffix());
        } else {
            return MrsResult.failed("登录已经过期！");
        }
        return MrsResult.ok("退出登录成功！");
    }
}
