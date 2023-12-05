package top.kirisamemarisa.sparkcipher.controller;

import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author Marisa
 * @Description 用户服务控制层
 * @Date 2023/11/27
 **/
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private SecurityUtils securityUtils;


    @GetMapping("/test")
    public MrsResult<?> test() {
        User authUser = securityUtils.getAuthUser();
        System.out.println(authUser);

        return MrsResult.ok();
    }

    @GetMapping("/list")
    public MrsResult<?> list() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
        return MrsResult.ok(list);
    }

    /**
     * 添加一个用户（注册）
     *
     * @param user .
     * @return .
     */
    @PostMapping("/add")
    public MrsResult<?> add(@RequestBody User user) {
        System.out.println("创建用户: " + user);
        // 校验用户名、密码、其它参数有则校验
        boolean b1 = user.verifyUserName();
        boolean b2 = user.verifyPassword();
        if (!b1 || !b2 || !user.verifyNullable()) return MrsResult.failed("校验未通过,请检查参数!");

        String salt = SaltGenerator.generateSalt();
        String pwd = user.getPassword();
        pwd = MD5Utils.md5(pwd + salt);
        String snowflakeId = IdUtils.nextIdOne();
        user.setId(snowflakeId);
        user.setLevel(1);
        user.setPassword(pwd);
        user.setSalt(salt);
        user.setCreateTime(new Date());
        user.setCreateBy(snowflakeId);
        boolean save = userService.save(user);
        return save ? MrsResult.ok() : MrsResult.failed();
    }

}
