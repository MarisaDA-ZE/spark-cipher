package top.kirisamemarisa.sparkcipher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import top.kirisamemarisa.sparkcipher.common.Constants;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Marisa
 * @Description 用户实体类
 * @Date 2023/11/27
 */
@Data
@ToString
public class User {
    // ID
    @TableId(type = IdType.AUTO)
    private String id;

    // 用户名
    private String userName;

    // 密码
    private String password;

    // 性别
    private Integer gender = 0;

    // 盐加密
    private String salt;

    // 手机号
    private String phone;

    // 等级
    private Integer level = 1;

    // 邮箱
    private String email;

    // 头像地址
    private String avatar;

    // 创建时间
    private Date createTime;

    // 创建者ID
    private String createBy;

    // 更新时间
    private Date updateTime;

    // 更新者ID
    private String updateBy;

    /**
     * 所有参数校验(不可为空)
     *
     * @return .
     */
    public boolean verify() {
        boolean b1 = verifyUserName();
        boolean b2 = verifyPassword();
        boolean b3 = verifyPhone();
        boolean b4 = verifyEmail();
        boolean b5 = verifyGender();
        boolean b6 = verifyLevel();
        return b1 && b2 && b3 && b4 && b5 && b6;
    }

    /**
     * 所有参数校验(可为空)
     *
     * @return .
     */
    public boolean verifyNullable() {
        boolean b1 = this.getUserName() == null || verifyUserName();
        boolean b2 = this.getPassword() == null || verifyPassword();
        boolean b3 = this.getPhone() == null || verifyPhone();
        boolean b4 = this.getEmail() == null || verifyEmail();
        boolean b5 = this.getGender() == null || verifyGender();
        boolean b6 = this.getLevel() == null || verifyLevel();
        return b1 && b2 && b3 && b4 && b5 && b6;
    }

    // 用户名校验
    public boolean verifyUserName() {
        //用户名正则，4到16位（字母，数字，下划线，减号,☆★）
        String regex = "[a-zA-Z0-9_-☆★]{4,16}";
        return this.regVerify(regex, this.getUserName());
    }

    // 密码校验
    public boolean verifyPassword() {
        // 至少6个字符，至少1个大写字母，1个小写字母和1个数字：
        String regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z-!@#&*$_.\\d]{6,}";
        return this.regVerify(regex, this.getPassword());
    }

    // 性别校验
    public boolean verifyGender() {
        return Constants.USER_GENDER.contains(this.getGender());
    }

    // 等级校验
    public boolean verifyLevel() {
        return Constants.USER_LEVELS.contains(this.getLevel());
    }

    // 手机号校验
    public boolean verifyPhone() {
        // 手机号码正则
        String regex = "(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1589]))\\d{8}";
        return this.regVerify(regex, this.getPhone());
    }

    // 邮箱校验
    public boolean verifyEmail() {
        //Email正则
        String regex = "([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,4})";
        return this.regVerify(regex, this.getEmail());
    }

    /**
     * 使用正则进行校验
     *
     * @param regExp 正则
     * @param target 目标字符串
     * @return .
     */
    protected boolean regVerify(String regExp, String target) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }
}
