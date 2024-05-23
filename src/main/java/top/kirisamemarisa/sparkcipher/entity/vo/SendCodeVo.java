package top.kirisamemarisa.sparkcipher.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Marisa
 * @Description 手机号验证码Vo
 * @Date 2024/5/10
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendCodeVo {
    private String account;     // 手机号
    private String code;        // 验证码
    private String msg;         // 提示信息
    private boolean status;     // 状态

    public static SendCodeVo success(String phoneNo, String code) {
        return new SendCodeVo(phoneNo, code, "验证码发送成功", true);
    }

    public static SendCodeVo failed(String msg) {
        return new SendCodeVo(null, null, msg, false);
    }
}
