package top.kirisamemarisa.sparkcipher.service;

import top.kirisamemarisa.sparkcipher.entity.LoginVo;
import top.kirisamemarisa.sparkcipher.entity.resp.MrsLResp;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Marisa
 * @Description ILogin.描述
 * @Date 2023/11/30
 */
public interface ILoginService {

    /**
     * 用户名密码登录
     *
     * @param loginVo 登录用户实体
     * @param req       HttpServletRequest
     * @return token
     */
    MrsLResp loginByAccount(LoginVo loginVo, HttpServletRequest req);
}
