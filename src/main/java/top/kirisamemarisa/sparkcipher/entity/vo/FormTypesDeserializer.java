package top.kirisamemarisa.sparkcipher.entity.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import top.kirisamemarisa.sparkcipher.entity.enums.FormTypes;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author Marisa
 * @Description 接受前端数据时的枚举转换器
 * @Date 2024/5/7
 */

public class FormTypesDeserializer extends JsonDeserializer<FormTypes> {

    @Override
    public FormTypes deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        return Arrays.stream(FormTypes.values())
                .filter(formType -> formType.getType().equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid FormTypes value: " + text));
    }
}
