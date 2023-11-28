package top.kirisamemarisa.sparkcipher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Marisa
 * @Description Record.描述
 * @Date 2023/11/28
 */
@Data
@ToString
public class Record {

    // 主键ID
    @TableId(type = IdType.AUTO)
    private String id;

    // 用户ID
    private String userId;

    // 标题
    private String title;

    // 账户名
    private String account;

    // 用户名
    private String userName;

    // 密码
    private String password;

    // 手机号
    private String phone;

    // 邮箱地址
    private String email;

    // 目标网址
    private String url;

    // 备注信息
    private String remark;

    // 创建时间
    private Date createTime;

    // 创建者ID
    private String createBy;

    // 更新时间
    private Date updateTime;

    // 更新时间
    private String updateBy;


    // 手机号校验
    public boolean verifyPhone() {
        // 手机号码正则
        String regex = "/^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$/";
        return this.regVerify(regex, this.getPhone());
    }

    // 邮箱校验
    public boolean verifyEmail() {
        //Email正则
        String regex = "/^([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,4})$/";
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
