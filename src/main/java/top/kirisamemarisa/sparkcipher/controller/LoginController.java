package top.kirisamemarisa.sparkcipher.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.enums.TypeSuffix;
import top.kirisamemarisa.sparkcipher.entity.*;
import top.kirisamemarisa.sparkcipher.entity.resp.MrsLResp;
import top.kirisamemarisa.sparkcipher.entity.vo.LoginVo;
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

    @PostMapping("/accountLogin")
    public MrsResult<?> login(@RequestBody LoginVo loginVo, HttpServletRequest req) {
        System.out.println("登录对象: " + loginVo);
        MrsLResp resp = loginService.loginByAccount(loginVo, req);
        return MrsResult.ok("登录成功！", resp);
    }

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
            return MrsResult.failed("登录已过期！");
        }
        return MrsResult.ok("退出登录成功！");
    }
}
