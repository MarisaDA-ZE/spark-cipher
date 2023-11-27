package top.kirisamemarisa.sparkcipher.controller;

import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.service.IUserService;
import top.kirisamemarisa.sparkcipher.util.IdUtil;

import javax.annotation.Resource;
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

    @GetMapping("/list")
    public MrsResult<?> list() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
        return MrsResult.ok(list);
    }

    /**
     * 添加一个用户
     *
     * @param user .
     * @return .
     */
    @PostMapping("/add")
    public MrsResult<?> add(@RequestBody User user) {
        user.setId(IdUtil.nextOne());
        user.setLevel(1);
        boolean save = userService.save(user);
        return save ? MrsResult.ok() : MrsResult.failed();
    }
}
