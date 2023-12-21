package top.kirisamemarisa.sparkcipher.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.aop.SM2Crypto;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.common.enums.TypeSuffix;
import top.kirisamemarisa.sparkcipher.entity.CryptoLoginVo;
import top.kirisamemarisa.sparkcipher.entity.LoginVo;
import top.kirisamemarisa.sparkcipher.entity.SM2KeyPair;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.service.ILoginService;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.TokenUtils;
import top.kirisamemarisa.sparkcipher.util.encrypto.sm2.SM2Utils;

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

    @Value("${mrs.enable-encrypt-link}")
    private Boolean enableEncrypt;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IUserService userService;

    @PostMapping("/accountLogin")
    public MrsResult<?> login(@RequestBody CryptoLoginVo cryptoLoginVo, HttpServletRequest req) {
        LoginVo loginVo = null;
        // 取出登录信息
        if (enableEncrypt) {
            String key = cryptoLoginVo.getF() + TypeSuffix.SKP.getTypeSuffix();
            SM2KeyPair skp = (SM2KeyPair) redisTemplate.opsForValue().get(key);
            if (skp != null) {
                String decrypt = SM2Utils.decrypt(cryptoLoginVo.getText(), skp.getPrivateKey());
                loginVo = JSONObject.parseObject(decrypt, LoginVo.class);
            }
        } else {
            loginVo = JSONObject.parseObject(cryptoLoginVo.getText(), LoginVo.class);
        }
        if (ObjectUtils.isNotEmpty(loginVo)) loginVo.setF(cryptoLoginVo.getF());
        System.out.println("登录对象: " + loginVo);
        String token = loginService.loginByAccount(loginVo, req);

        // 附上对应的用户信息
        String uid = TokenUtils.decryptToken(token, "account");
        Map<String, Object> res = new HashMap<>();
        if (uid != null) {
            User authUser = (User) redisTemplate.opsForValue().get(uid);
            if (authUser != null) {
                authUser.setSalt(null);
                authUser.setPassword(null);
                res.put("user", authUser);
            }
        }
        res.put("token", token);
        return MrsResult.ok("登录成功！", res);
    }
}
