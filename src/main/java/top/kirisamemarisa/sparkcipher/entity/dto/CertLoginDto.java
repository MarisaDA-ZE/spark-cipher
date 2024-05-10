package top.kirisamemarisa.sparkcipher.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Marisa
 * @Description CertLoginDto.描述
 * @Date 2024/5/10
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CertLoginDto {
    private boolean status;
    private String msg;
}
