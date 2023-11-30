package top.kirisamemarisa.sparkcipher.util.sm2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Marisa
 * @Description SM2密钥对
 * @Date 2023/11/29
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SM2KeyPair {
    String publicKey;   // 公钥
    String privateKey;  // 私钥
}
