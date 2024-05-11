package top.kirisamemarisa.sparkcipher.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import top.kirisamemarisa.sparkcipher.annotations.DBCrypto;
import top.kirisamemarisa.sparkcipher.annotations.RecordTranslate;
import top.kirisamemarisa.sparkcipher.aop.enums.FIELD_TYPE;
import top.kirisamemarisa.sparkcipher.entity.vo.RecordVo;
import top.kirisamemarisa.sparkcipher.util.FileUtil;
import top.kirisamemarisa.sparkcipher.util.encrypto.aes.AES256Utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Marisa
 * @Description Record.描述
 * @Date 2023/11/28
 */
@Data
@ToString
public class Record {

    // 主键ID
    @TableId(type = IdType.AUTO)
    private String id;

    // 用户ID
    private String userId;

    // 标题
    @DBCrypto
    @RecordTranslate
    private String title;

    // 搜索关键字（标题的内容）
    private String searchText;

    // 账户名
    @DBCrypto
    @RecordTranslate
    private String account;

    // 用户名
    @DBCrypto
    @RecordTranslate
    private String nickName;

    // 密码
    @DBCrypto
    @RecordTranslate
    private String password;

    // 手机号
    @DBCrypto
    @RecordTranslate
    private String phone;

    // 邮箱地址
    @DBCrypto
    @RecordTranslate
    private String email;

    // 目标网址
    @DBCrypto
    @RecordTranslate
    private String url;

    // 备注信息
    @DBCrypto
    @RecordTranslate
    private String remark;

    // 自定义属性
    @DBCrypto
    @RecordTranslate(FIELD_TYPE.LIST_M_RECORD_ITEM)
    private String customs;

    // 创建时间
    @RecordTranslate(FIELD_TYPE.M_DATE)
    private Date createTime;

    // 创建者ID
    private String createBy;

    // 更新时间
    @RecordTranslate(FIELD_TYPE.M_DATE)
    private Date updateTime;

    // 更新时间
    private String updateBy;

    // 手机号校验
    public boolean verifyPhone() {
        // 手机号码正则
        String regex = "(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1589]))\\d{8}";
        return this.regVerify(regex, this.getPhone());
    }

    // 邮箱校验
    public boolean verifyEmail() {
        //Email正则
        String regex = "([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,4})";
        return this.regVerify(regex, this.getEmail());
    }

    // 网址校验
    public boolean verifyUrl() {
        //Email正则
        String regex = "(http|https|ftp)://((((25[0-5])|(2[0-4]\\d)|(1\\d{2})|([1-9]?\\d)\\.){3}" +
                "((25[0-5])|(2[0-4]\\d)|(1\\d{2})|([1-9]?\\d)))|(([\\w-]+\\.)+" +
                "(net|com|org|gov|edu|mil|info|travel|pro|museum|biz|[a-z]{2})))" +
                "(/[\\w\\-~#]+)*(/[\\w-]+\\.[\\w]{2,4})?([\\?=&%_]?[\\w-]+)*";
        return this.regVerify(regex, this.getUrl());
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

    /**
     * 将对象转换为VO
     *
     * @return recordVo对象
     */
    public RecordVo toVo() {
        Record record = this;
        RecordVo recordVo = new RecordVo();
        Class<? extends Record> clazz = record.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String name = field.getName();
                Field voField = RecordVo.class.getDeclaredField(name);
                voField.setAccessible(true);

                Object value = field.get(record);
                RecordTranslate annotation = field.getAnnotation(RecordTranslate.class);
                if (annotation != null) {
                    switch (annotation.value()) {
                        case M_RECORD_ITEM:
                            RecordItem recordItem = JSONObject.parseObject((String) value, RecordItem.class);
                            voField.set(recordVo, recordItem);
                            break;
                        case LIST_M_RECORD_ITEM:
                            TypeReference<?> typeRef = new TypeReference<List<RecordItem>>() {
                            };
                            Object list = JSONObject.parseObject((String) value, typeRef);
                            voField.set(recordVo, list);
                            break;
                        case M_DATE:
                            try {
                                voField.set(recordVo, ((Date) value).getTime());
                            } catch (NullPointerException ex) {
                                voField.set(recordVo, null);
                            }
                            break;
                    }
                } else {
                    voField.set(recordVo, value);
                }
            } catch (IllegalAccessException e) {
                System.out.println("错误信息: " + e.getMessage());
            }catch (NoSuchFieldException ignored){
                // 这种情况是没有vo类中没有dto对应的字段，直接不管即可
            }
        }
        return recordVo;
    }

    /**
     * 数据加密
     * <p>将对象属性中带有 DBCrypto 注解的字段进行加密</p>
     */
    public void encryptField() {
        Record record = this;
        Class<? extends Record> clazz = record.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            DBCrypto annotation = field.getAnnotation(DBCrypto.class);
            Object value;
            try {
                value = field.get(record);
                if (annotation != null && value instanceof String) {
                    if ("null".equals(value)) {
                        value = null;
                    } else {
                        value = AES256Utils.encrypt((String) value);
                    }
                }
                field.set(record, value);
            } catch (Exception e) {
                System.out.println("出错了: " + e.getMessage());
            }
        }
    }

    /**
     * 数据解密
     * <p>将对象属性中带有 DBCrypto 注解的字段进行解密</p>
     */
    public void decryptField() {
        Record record = this;
        Class<? extends Record> clazz = record.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            DBCrypto annotation = field.getAnnotation(DBCrypto.class);
            Object value;
            try {
                value = field.get(record);
                if (annotation != null && value instanceof String) {
                    value = AES256Utils.decrypt((String) value);
                }
                field.set(record, value);
            } catch (Exception e) {
                System.out.println("出错了: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String path = "D:\\Codes\\spark-cipher\\src\\main\\java\\top\\kirisamemarisa\\sparkcipher\\json\\recordMock.json";
        String content = FileUtil.readFileContent(path);
        Record record = JSONObject.parseObject(content, Record.class);
        System.out.println(record);
        if (record != null) {
            record.decryptField();
            RecordVo recordVo = record.toVo();
            System.out.println(recordVo);
        }
        System.out.println(record);
    }

}
