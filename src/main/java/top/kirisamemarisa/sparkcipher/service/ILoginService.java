package top.kirisamemarisa.sparkcipher.service;

import top.kirisamemarisa.sparkcipher.entity.LoginUser;

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
     * @param loginUser 登录用户实体
     * @param req       HttpServletRequest
     * @return token
     */
    String loginByAccount(LoginUser loginUser, HttpServletRequest req);
}
