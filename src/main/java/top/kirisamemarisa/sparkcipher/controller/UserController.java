package top.kirisamemarisa.sparkcipher.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.entity.vo.UserVo;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.*;

import javax.annotation.Resource;

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

    /**
     * 测试接口
     *
     * @return .
     */
    @GetMapping("/test")
    public MrsResult<?> test() {
        User authUser = securityUtils.getAuthUser();
        System.out.println("登录用户: " + authUser);
        return MrsResult.ok();
    }

    /**
     * 获取当前登录用户的信息
     * @return  userVo
     */
    @GetMapping("/getUserInfo")
    public MrsResult<?> getUserInfo() {
        User authUser = securityUtils.getAuthUser();
        System.out.println("登录用户: " + authUser);
        String uid = authUser.getId();
        User dbUser = userService.getById(uid);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(dbUser, userVo);
        return MrsResult.ok(userVo);
    }
}
