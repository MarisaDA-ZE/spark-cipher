package top.kirisamemarisa.sparkcipher.entity;

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
    private String id;
    private String userName;
    private String gender;
    private String phone;
    private Integer level;
    private String email;
    private String avatar;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
}
