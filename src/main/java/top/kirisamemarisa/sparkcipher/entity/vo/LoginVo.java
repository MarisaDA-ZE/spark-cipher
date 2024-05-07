package top.kirisamemarisa.sparkcipher.entity.vo;

import lombok.Data;
import lombok.ToString;
import top.kirisamemarisa.sparkcipher.entity.enums.LoginType;

import java.util.Date;

/**
 * @Author Marisa
 * @Description LoginUser.描述
 * @Date 2023/11/30
 */
@Data
@ToString
public class LoginVo {
    private String account;     // 账户
    private String password;    // 密码
    private String phoneNo;     // 手机号
    private String email;       // 邮箱
    private String verifyCode;  // 验证码
    private LoginType loginType;// 登录方式
    private String f;           // 唯一标识
    private String loginIp;     // 登录IP
    private String loginMac;    // 设备MAC地址
    private Date loginTime;     // 登录时间

}
