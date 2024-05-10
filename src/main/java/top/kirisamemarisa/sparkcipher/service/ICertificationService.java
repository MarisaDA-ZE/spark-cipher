package top.kirisamemarisa.sparkcipher.service;

import top.kirisamemarisa.sparkcipher.entity.dto.CertLoginDto;

/**
 * @Author Marisa
 * @Description 认证中心接口
 * @Date 2024/5/10
 */
public interface ICertificationService {

    /**
     * 验证token有效性
     * @param token 前端token
     * @return  有效信息
     */
    CertLoginDto verifyTokenInfo(String token);
}
