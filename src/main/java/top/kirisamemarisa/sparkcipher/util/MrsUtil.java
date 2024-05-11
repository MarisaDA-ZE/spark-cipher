package top.kirisamemarisa.sparkcipher.util;

import com.baomidou.mybatisplus.annotation.TableField;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author Marisa
 * @Description 工具类
 * @Date 2024/5/10
 */
public class MrsUtil {
    // 验证码所用到的字符
    private static final String[] KEY_CHARS = new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    /**
     * 生成length位的纯数字验证码
     *
     * @param length 验证码长度
     * @return 生成的验证码
     */
    public static String getRandomCodeNum(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int ix = random.nextInt(10);
            sb.append(ix);
        }
        return sb.toString();
    }

    /**
     * 生成length位的混合验证码
     *
     * @param length 验证码长度
     * @return 生成的验证码
     */
    public static String getRandomCodeStr(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int ix = random.nextInt(KEY_CHARS.length);
            sb.append(KEY_CHARS[ix]);
        }
        return sb.toString();
    }

    /**
     * 校验手机号是否合规
     *
     * @param phoneNo 手机号
     * @return 校验结果（true:合规，false:不合规）
     */
    public static boolean verifyPhoneNo(String phoneNo) {
        String regExp = "(?:(?:\\+|00)86)?1(?:3\\d|4[5-79]|5[0-35-9]|6[5-7]|7[0-8]|8\\d|9[1589])\\d{8}";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(phoneNo);
        return matcher.matches();
    }

    /**
     * 校验邮箱是否合规
     *
     * @param email 邮箱号
     * @return 校验结果（true:合规，false:不合规）
     */
    public static boolean verifyEmail(String email) {
        String regExp = "([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,4})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static <T> String columnNameTranslate(String column, T entity) {
        // 获取实体类的Class对象
        Class<?> clazz = entity.getClass();

        Field[] fields = clazz.getDeclaredFields();
        Set<String> keySet = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toSet());
        if (!keySet.contains(column)) return null;
        for (Field field : fields) {
            String name = field.getName();
            if ((name + "").equals(column)) {
                TableField annotation = field.getAnnotation(TableField.class);
                String fieldName = name;
                if (annotation != null) {
                    fieldName = annotation.value();
                }
                return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
            }
        }
        // 字段不存在或没有@TableField注解时返回空
        return null;
    }

}
