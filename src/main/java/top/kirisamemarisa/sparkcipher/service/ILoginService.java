package top.kirisamemarisa.sparkcipher.service;

import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.entity.vo.LoginVo;
import top.kirisamemarisa.sparkcipher.entity.resp.MrsLResp;
import top.kirisamemarisa.sparkcipher.entity.vo.SendCodeVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Marisa
 * @Description ILogin.描述
 * @Date 2023/11/30
 */
public interface ILoginService {

    /**
     * 通过用户名密码创建账号
     *
     * @param loginVo vo类
     * @return 创建结果（错误信息或者null，null时表示创建成功）
     */
    String accountCreate(LoginVo loginVo);

    /**
     * 用户名密码登录
     *
     * @param loginVo 登录用户实体
     * @param req     HttpServletRequest
     * @return token
     */
    MrsLResp loginByAccount(LoginVo loginVo, HttpServletRequest req);

    /**
     * 发送验证码
     *
     * @param phoneNo 手机号
     * @return 发送结果
     */
    SendCodeVo sendCodePhone(String phoneNo);

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 发送结果
     */
    SendCodeVo sendEmailCode(String email);

    /**
     * 根据key查询是否有重复项
     * <p>这个方法只会匹配User对象中含有@UniqueField注解且值是非空的键</p>
     *
     * @param user 用户实体对象
     * @return 键值重复数量
     */
    int getCountByKey(User user);

    /**
     * 手机号验证码登录
     *
     * @param loginVo 登录用户实体
     * @param req     HttpServletRequest
     * @return token
     */
    MrsLResp loginByPhone(LoginVo loginVo, HttpServletRequest req);
}
