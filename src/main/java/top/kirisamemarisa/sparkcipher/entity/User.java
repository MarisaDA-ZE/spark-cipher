package top.kirisamemarisa.sparkcipher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

/**
 * @Author Marisa
 * @Description 用户实体类
 * @Date 2023/11/27
 */
@Data
@ToString
public class User {
    @TableId(type = IdType.AUTO)
    private String id;  // ID
    private String userName;// 名字

    private String password;// 密码
    private Integer level;// 等级
}
