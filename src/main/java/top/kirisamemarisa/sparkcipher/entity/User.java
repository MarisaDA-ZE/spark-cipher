package top.kirisamemarisa.sparkcipher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import top.kirisamemarisa.sparkcipher.annotations.UniqueField;
import top.kirisamemarisa.sparkcipher.common.Constants;
import top.kirisamemarisa.sparkcipher.entity.vo.UserVo;

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

    // 账号
    @UniqueField
    private String account;

    // 昵称
    private String nickName;

    // 密码
    private String password;

    // 性别
    private Integer gender = 0;

    // 盐加密
    private String salt;

    // 手机号
    @UniqueField
    private String phone;

    // 原始注册手机号
    @UniqueField
    private String originalPhone;

    // 等级
    private Integer level = 1;

    // 邮箱
    @UniqueField
    private String email;

    // 原始注册邮箱
    @UniqueField
    private String originalEmail;

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
        boolean b1 = verifyAccount();
        boolean b2 = verifyNickName();
        boolean b3 = verifyPassword();
        boolean b4 = verifyPhone();
        boolean b5 = verifyEmail();
        return b1 && b2 && b3 && (b4 || b5);
    }

    /**
     * 所有参数校验(可为空)
     *
     * @return .
     */
    public boolean verifyNullable() {
        boolean b1 = this.getAccount() == null || verifyAccount();
        boolean b2 = this.getPassword() == null || verifyPassword();
        boolean b3 = this.getPhone() == null || verifyPhone();
        boolean b4 = this.getEmail() == null || verifyEmail();
        return b1 && b2 && (b3 || b4);
    }

    // 账号正则校验
    public boolean verifyAccount() {
        // if (StringUtils.isBlank(this.getAccount())) return false;
        //用户名正则，4到16位（字母，数字，下划线，减号,☆★）
        String regex = "[a-zA-Z0-9_-☆★]{4,16}";
        return this.regVerify(regex, this.getAccount());
    }

    public boolean verifyAccount(String userName) {
        // if (StringUtils.isBlank(this.getAccount())) return false;
        //用户名正则，4到16位（字母，数字，下划线，减号,☆★）
        String regex = "[a-zA-Z0-9_-☆★]{4,16}";
        return this.regVerify(regex, userName);
    }

    // 昵称正则校验
    public boolean verifyNickName() {
        // if (StringUtils.isBlank(this.getNickName())) return false;
        //用户名正则，4到16位（字母，数字，下划线，减号,☆★）
        String regex = "[a-zA-Z0-9_-☆★]{4,16}";
        return this.regVerify(regex, this.getNickName());
    }

    public boolean verifyNickName(String nickName) {
        // if (StringUtils.isBlank(this.getNickName())) return false;
        //用户名正则，4到16位（字母，数字，下划线，减号,☆★）
        String regex = "[a-zA-Z0-9_-☆★]{4,16}";
        return this.regVerify(regex, nickName);
    }

    // 密码校验
    public boolean verifyPassword() {
        // if (StringUtils.isBlank(this.getPassword())) return false;
        // 至少6个字符，至少1个大写字母，1个小写字母和1个数字：
        String regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z-!@#&*$_.\\d]{6,}";
        return this.regVerify(regex, this.getPassword());
    }

    public boolean verifyPassword(String password) {
        // if (StringUtils.isBlank(this.getPassword())) return false;
        // 至少6个字符，至少1个大写字母，1个小写字母和1个数字：
        String regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z-!@#&*$_.\\d]{6,}";
        return this.regVerify(regex, password);
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
        // if (StringUtils.isBlank(this.getPhone())) return false;
        // 手机号码正则
        String regex = "(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1589]))\\d{8}";
        return this.regVerify(regex, this.getPhone());
    }

    // 邮箱校验
    public boolean verifyEmail() {
        // if (StringUtils.isBlank(this.getEmail())) return false;
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

    /**
     * dto转vo
     * @return  userVo
     */
    public UserVo toVo(){
        UserVo userVo = new UserVo();
        System.out.println(this.getGender());
        BeanUtils.copyProperties(this, userVo);
        Date created = this.getCreateTime();
        Date updated = this.getUpdateTime();
        userVo.setGender(this.getGender().toString());
        if(created != null) userVo.setCreateTime(created.getTime());
        if(updated != null) userVo.setUpdateTime(updated.getTime());
        return userVo;
    }
}
