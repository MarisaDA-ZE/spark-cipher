package top.kirisamemarisa.sparkcipher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String label;   // 标签（提示文字）
    private String key;     // 键名
    private String value;   // 值
    private String type;    // 表单类型
    private Integer sort;   // 排序字段
}
