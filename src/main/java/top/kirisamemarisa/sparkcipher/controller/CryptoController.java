package top.kirisamemarisa.sparkcipher.controller;

import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.NegotiateKeyPair;
import top.kirisamemarisa.sparkcipher.service.ICryptoService;

import javax.annotation.Resource;

/**
 * @Author Marisa
 * @Description CryptoController.描述
 * @Date 2023/12/8
 */
@CrossOrigin
@RestController
@RequestMapping("/crypto")
public class CryptoController {

    @Resource
    private ICryptoService cryptoService;

    /**
     * 获取服务端公钥
     *
     * @param fingerPrint 客户端指纹
     * @return 私钥签名的公钥
     */
    @GetMapping("/getPublicKey")
    public MrsResult<?> getPublicKey(@RequestParam(name = "f") String fingerPrint) {
        String publicKey = cryptoService.getPublicKey(fingerPrint);
        System.out.println("公钥: " + publicKey);
        return MrsResult.ok(publicKey);
    }

    /**
     * 协商密钥对
     *
     * @return 服务端响应请求的公钥
     */
    @PostMapping("/negotiateKeyPair")
    public MrsResult<?> negotiateKeyPair(@RequestBody NegotiateKeyPair keyPair) {
        String key = cryptoService.negotiateKeyPair(keyPair);
        return MrsResult.ok("公钥获取成功", key);
    }
}
