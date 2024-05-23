package top.kirisamemarisa.sparkcipher.common;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Marisa
 * @Description 常量
 * @Date 2023/11/28
 */
public class Constants {

    // token的过期时间(单位秒)
    public static final Integer TOKEN_EXPIRE_TIME = 10 * 60;
    // 密钥对过期时间
    public static final Integer KEYPAIR_EXPIRE_TIME = 30 * 60;

    // 一天中手机验证码的最大发送次数
    public static final Integer PHONE_CODE_COUNT = 15;  // 15
    public static final Integer EMAIL_CODE_COUNT = 1000;// 15
    // 手机验证码过期时间(15分钟)
    public static final Integer PHONE_CODE_EXPIRE_TIME = 1000 * 60 * 15;

    // 一天所有的秒数
    public static final Integer ONE_DAY_SECONDS = 60 * 60 * 24;

    // redis中token类的后缀
    public static final String USER_SUFFIX = ".user";
    public static final String TOKEN_SUFFIX = ".token";
    public static final String PHONE_VERIFY_SUFFIX = ".phone-verify-code";
    public static final String EMAIL_VERIFY_SUFFIX = ".email-verify-code";

    // 性别 0.未知性别,1.男性,2.女性
    public static final List<Integer> USER_GENDER = Arrays.asList(0, 1, 2);

    // 用户等级 1~6级
    public static final List<Integer> USER_LEVELS = Arrays.asList(1, 2, 3, 4, 5, 6);

}
