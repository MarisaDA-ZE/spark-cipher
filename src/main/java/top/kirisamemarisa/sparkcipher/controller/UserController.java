package top.kirisamemarisa.sparkcipher.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.service.IUserService;

import javax.annotation.Resource;
import java.util.List;

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
}
