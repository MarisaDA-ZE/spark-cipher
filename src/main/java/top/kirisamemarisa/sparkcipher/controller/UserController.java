package top.kirisamemarisa.sparkcipher.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.annotations.UniqueField;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.*;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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




}
