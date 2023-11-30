package top.kirisamemarisa.sparkcipher.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * JWT生成token
 */
public class TokenUtils {
    public static final int EXPIRE_TIME = 30;                   // 设置签名过期时间(分钟)

    // "Kirisame.MarisaDA☆ZE"的MD5值
    public static final String TOKEN_SECRET = "680445a7e4f5ab6a428828893e2f3c36";    // 设置签名私钥


    /**
     * 生成token，可以将你认为需要的数据当成参数存入
     *
     * @param account  账户名
     * @param deviceId 设备ID
     * @param time     生成时间
     * @return 生成的token
     */
    public static String sign(String account, String deviceId, String time) {
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("deviceId", deviceId);
        map.put("time", time);
        return sign(map);
    }

    /**
     * 生成token
     *
     * @param map .
     * @return .
     */
    public static String sign(Map<String, String> map) {
        String result;
        Set<String> keys = map.keySet();
        try {
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");

            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, EXPIRE_TIME);  //当前时间 + 过期分钟
            Date expireDate = now.getTime();        //Calendar转Date
            //设置JWT生效时间
            Date nowDate = new Date();              //系统当前时间
            // 返回token字符串
            JWTCreator.Builder builder = JWT.create().withHeader(header);
            for (String key : keys) {
                builder.withClaim(key, map.get(key));
            }
            result = builder
                    .withExpiresAt(expireDate)  //过期时间
                    .withNotBefore(nowDate)     //生效时间
                    .sign(algorithm);//添加签名信息
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检验token是否正确语句
     *
     * @param token token字符串
     * @return 校验结果
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取token中的参数值
     *
     * @param token token字符串
     * @param key   键
     * @return token中的信息
     */
    public static String decryptToken(String token, String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        String token = sign("18384669885", "154338944", String.valueOf(l));
        String account = decryptToken(token, "account");
        String deviceId = decryptToken(token, "deviceId");
        String time = decryptToken(token, "time");
        boolean verify = verify(token);
        System.out.println(token);

        assert time != null;
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        System.out.println("account: " + account + ",deviceId: " + deviceId + ",校验是否通过: " + verify);
        System.out.println(format);
    }
}