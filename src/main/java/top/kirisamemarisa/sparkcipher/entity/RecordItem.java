package top.kirisamemarisa.sparkcipher.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.kirisamemarisa.sparkcipher.entity.enums.FormTypes;
import top.kirisamemarisa.sparkcipher.entity.vo.FormTypesDeserializer;

/**
 * @Author Marisa
 * @Description RecordItem.描述
 * @Date 2024/5/7
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecordItem {
    private String label;
    private String key;
    private String value;

    @JsonDeserialize(using = FormTypesDeserializer.class)
    private FormTypes type;
    private Integer sort;
}
