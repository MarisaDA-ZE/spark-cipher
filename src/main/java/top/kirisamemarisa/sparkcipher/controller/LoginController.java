package top.kirisamemarisa.sparkcipher.controller;

import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.LoginUser;
import top.kirisamemarisa.sparkcipher.service.ILoginService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @PostMapping("/accountLogin")
    public MrsResult<?> login(@RequestBody LoginUser user, HttpServletRequest req) {
        String token = loginService.loginByAccount(user, req);
        return MrsResult.ok("登录成功！", token);
    }
}
