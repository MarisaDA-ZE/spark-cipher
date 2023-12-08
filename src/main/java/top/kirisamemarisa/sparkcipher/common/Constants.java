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
    public static final Integer TOKEN_EXPIRE_TIME = 30 * 60;
    // 密钥对过期时间
    public static final Integer KEYPAIR_EXPIRE_TIME = 30 * 60;

    // 性别 0.未知性别,1.男性,2.女性
    public static final List<Integer> USER_GENDER = Arrays.asList(0, 1, 2);

    // 用户等级 1~6级
    public static final List<Integer> USER_LEVELS = Arrays.asList(1, 2, 3, 4, 5, 6);
}
