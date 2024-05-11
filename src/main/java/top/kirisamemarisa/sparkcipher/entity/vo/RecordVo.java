package top.kirisamemarisa.sparkcipher.entity.vo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import top.kirisamemarisa.sparkcipher.annotations.RecordTranslate;
import top.kirisamemarisa.sparkcipher.aop.enums.FIELD_TYPE;
import top.kirisamemarisa.sparkcipher.entity.Record;
import top.kirisamemarisa.sparkcipher.entity.RecordItem;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Marisa
 * @Description 记录VO
 * @Date 2024/5/7
 */
@Data
@ToString
public class RecordVo {

    // 主键ID
    @TableId(type = IdType.AUTO)
    private String id;

    // 用户ID
    private String userId;

    // 标题
    @RecordTranslate
    private RecordItem title;

    // 账户名
    @RecordTranslate
    private RecordItem account;

    // 用户名
    @RecordTranslate
    private RecordItem nickName;

    // 密码
    @RecordTranslate
    private RecordItem password;

    // 手机号
    @RecordTranslate
    private RecordItem phone;

    // 邮箱地址
    @RecordTranslate
    private RecordItem email;

    // 目标网址
    @RecordTranslate
    private RecordItem url;

    // 备注信息
    @RecordTranslate
    private RecordItem remark;

    // 自定义属性
    @RecordTranslate(FIELD_TYPE.LIST_M_RECORD_ITEM)
    private List<RecordItem> customs;

    // 创建时间
    @RecordTranslate(FIELD_TYPE.M_DATE)
    private Long createTime;

    // 创建者ID
    private String createBy;

    // 更新时间
    @RecordTranslate(FIELD_TYPE.M_DATE)
    private Long updateTime;

    // 更新时间
    private String updateBy;


    // 手机号校验
    public boolean verifyPhone() {
        // 手机号码正则
        String regex = "(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1589]))\\d{8}";
        return this.regVerify(regex, this.getPhone().getValue());
    }

    // 邮箱校验
    public boolean verifyEmail() {
        //Email正则
        String regex = "([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,4})";
        return this.regVerify(regex, this.getEmail().getValue());
    }

    // 网址校验
    public boolean verifyUrl() {
        //Email正则
        String regex = "(http|https|ftp)://((((25[0-5])|(2[0-4]\\d)|(1\\d{2})|([1-9]?\\d)\\.){3}" +
                "((25[0-5])|(2[0-4]\\d)|(1\\d{2})|([1-9]?\\d)))|(([\\w-]+\\.)+" +
                "(net|com|org|gov|edu|mil|info|travel|pro|museum|biz|[a-z]{2})))" +
                "(/[\\w\\-~#]+)*(/[\\w-]+\\.[\\w]{2,4})?([\\?=&%_]?[\\w-]+)*";
        return this.regVerify(regex, this.getUrl().getValue());
    }

    /**
     * 使用正则进行校验
     *
     * @param regExp 正则
     * @param target 目标字符串
     * @return .
     */
    protected boolean regVerify(String regExp, String target) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public Record toDto() {
        RecordVo vo = this;
        Class<? extends RecordVo> clazz = vo.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Record record = new Record();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                Field dtoField = Record.class.getDeclaredField(name);
                dtoField.setAccessible(true);
                Object value = field.get(vo);
                RecordTranslate annotation = field.getAnnotation(RecordTranslate.class);
                if (annotation != null) {
                    switch (annotation.value()) {
                        case M_RECORD_ITEM:
                        case LIST_M_RECORD_ITEM:
                            dtoField.set(record, JSONObject.toJSONString(value));
                            break;
                        case M_DATE:
                            if (value == null) {
                                dtoField.set(record, null);
                            } else {
                                dtoField.set(record, new Date((long) value));
                            }
                            break;
                    }
                } else {
                    dtoField.set(record, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                System.out.println("错误信息: " + e.getMessage());
            }
        }
        return record;
    }
}
