package top.kirisamemarisa.sparkcipher.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author Marisa
 * @Description NegotiateKeyPair.描述
 * @Date 2023/12/21
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class NegotiateKeyPair extends SM2KeyPair {
    private String f;  // 设备指纹
}
