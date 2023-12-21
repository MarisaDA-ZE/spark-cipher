package top.kirisamemarisa.sparkcipher.entity;

import lombok.Data;
import lombok.ToString;
import top.kirisamemarisa.sparkcipher.common.enums.LoginType;

import java.util.Date;

/**
 * @Author Marisa
 * @Description CryptoLoginVo.描述
 * @Date 2023/12/21
 */
@Data
@ToString
public class CryptoLoginVo {
    private String text;     // 密文或明文JSON
    private String f;        // 设备标识
}
