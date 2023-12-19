package top.kirisamemarisa.sparkcipher.controller;

import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.LoginUser;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.service.ILoginService;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Marisa
 * @Description 登录服务
 * @Date 2023/11/30
 */

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class LoginController {

    @Resource
    private ILoginService loginService;

    @Resource
    private IUserService userService;


    @PostMapping("/accountLogin")
    public MrsResult<?> login(@RequestBody LoginUser user, HttpServletRequest req) {
        String token = loginService.loginByAccount(user, req);
        String id = TokenUtils.decryptToken(token, "account");
        User authUser = userService.getById(id);
        authUser.setSalt(null);
        authUser.setPassword(null);
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("user", authUser);
        return MrsResult.ok("登录成功！", res);
    }
}
