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
public class PhoneCodeVo {
    private String phoneNo;     // 手机号
    private String code;        // 验证码
    private String msg;         // 提示信息
    private boolean status;     // 状态

    public static PhoneCodeVo success(String phoneNo, String code) {
        return new PhoneCodeVo(phoneNo, code, "验证码发送成功", true);
    }

    public static PhoneCodeVo failed(String msg) {
        return new PhoneCodeVo(null, null, msg, false);
    }
}
