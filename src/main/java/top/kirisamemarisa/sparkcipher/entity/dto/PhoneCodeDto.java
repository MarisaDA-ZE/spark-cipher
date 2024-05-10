package top.kirisamemarisa.sparkcipher.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Marisa
 * @Description VerifyCodeDto.描述
 * @Date 2024/5/10
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhoneCodeDto {
    private String phoneNo;     // 手机号
    private String code;        // 最新的验证码
    private int remainingCount; // 剩余发送次数
    private long lastSendTime;  // 上次发送时间
}
