package top.kirisamemarisa.sparkcipher.entity.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Author Marisa
 * @Description UserVo.描述
 * @Date 2023-12-21
 */
@Data
@ToString
public class UserVo {
    private String id;          // ID
    private String account;    // 用户名
    private String nickName;    // 用户名
    private String gender;      // 性别
    private String phone;       // 手机号
    private Integer level;      // 等级
    private String email;       // 邮箱
    private String avatar;      // 头像
    private Long createTime;    // 创建时间
    private String createBy;    // 创建者ID
    private Long updateTime;    // 最近更新时间
    private String updateBy;    // 更新者ID
}
