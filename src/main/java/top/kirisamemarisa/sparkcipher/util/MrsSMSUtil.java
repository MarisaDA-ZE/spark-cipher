package top.kirisamemarisa.sparkcipher.util;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.models.*;
import com.aliyun.sdk.service.dysmsapi20170525.*;

import darabonba.core.client.ClientOverrideConfiguration;
import top.kirisamemarisa.sparkcipher.annotations.StaticValue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author Marisa
 * @Description 短信服务工具类
 * @Date 2024/5/22
 */
public class MrsSMSUtil {

    @StaticValue("mrs.alibaba.access-name")
    private static String ACCESS_NAME;
    @StaticValue("mrs.alibaba.access-key-id")
    private static String ACCESS_KEY_ID;
    @StaticValue("mrs.alibaba.access-key-secret")
    private static String ACCESS_KEY_SECRET;

    private static final String TEMPLATE_CODE = "SMS_465971659";

     static {
        String fileName = "private/mrs-alibaba.properties";
        MrsPropertiesReader.loadProperties(fileName, MrsSMSUtil.class);
     }


    private static AsyncClient getClient() {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(ACCESS_KEY_ID)
                .accessKeySecret(ACCESS_KEY_SECRET)
                .build());
        return AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration
                                .create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
    }

    /**
     * 向指定手机号发送验证码
     * @param phoneNo   手机号
     * @param code  验证码
     * @return  是否发送成功
     * @throws ExecutionException .
     * @throws InterruptedException .
     */
    public static boolean sendPhoneCode(String phoneNo, String code) throws ExecutionException, InterruptedException {
        AsyncClient client = getClient();
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(ACCESS_NAME)
                .templateCode(TEMPLATE_CODE)
                .phoneNumbers(phoneNo)
                .templateParam("{\"code\":\""+code+"\"}")
                .build();
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = response.get();
        client.close();
        String res = JSONObject.toJSONString(resp.getBody());
        JSONObject body = JSONObject.parseObject(res);
        String resCode = body.getString("code");
        boolean result = "OK".equals(resCode);
        if(!result) {
            System.out.println("发送失败了...");
            System.out.println("Body: " + body);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(MrsSMSUtil.ACCESS_NAME);
        System.out.println(MrsSMSUtil.ACCESS_KEY_ID);
        System.out.println(MrsSMSUtil.ACCESS_KEY_SECRET);
        // boolean isSuccess = sendPhoneCode("18384669885", "526785");
    }

}
