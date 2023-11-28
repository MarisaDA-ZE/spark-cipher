package top.kirisamemarisa.sparkcipher.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.Record;
import top.kirisamemarisa.sparkcipher.service.IRecordService;
import top.kirisamemarisa.sparkcipher.util.AES256Encryption;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author Marisa
 * @Description 用户服务控制层
 * @Date 2023/11/27
 **/
@RestController
@CrossOrigin
@RequestMapping("/record")
public class RecordController {

    @Resource
    private IRecordService recordService;

    @Resource
    private AES256Encryption aesUtil;

    @GetMapping("/test")
    public MrsResult<?> test() {
        String s = "原神,启动!";
        String encrypt = aesUtil.encrypt(s);
        System.out.println(encrypt);
        String decrypt = aesUtil.decrypt(encrypt);
        System.out.println(decrypt);
        return MrsResult.ok();
    }

    /**
     * 添加一条记录
     *
     * @param record .
     * @return .
     */
    @PostMapping("/add")
    public MrsResult<?> add(@RequestBody Record record) {
        System.out.println(record);
        if (ObjectUtils.isEmpty(record)) return MrsResult.failed();

        // 标题加密
        if (StringUtils.isNotBlank(record.getTitle()))
            record.setTitle(aesUtil.encrypt(record.getTitle()));

        // 账户名加密
        if (StringUtils.isNotBlank(record.getAccount()))
            record.setAccount(aesUtil.encrypt(record.getAccount()));

        // 用户名加密
        if (StringUtils.isNotBlank(record.getUserName()))
            record.setUserName(aesUtil.encrypt(record.getUserName()));

        // 密码加密
        if (StringUtils.isNotBlank(record.getPassword()))
            record.setPassword(aesUtil.encrypt(record.getPassword()));

        // 手机号加密
        if (StringUtils.isNotBlank(record.getPhone()) && record.verifyPhone())
            record.setPhone(aesUtil.encrypt(record.getPhone()));

        // 邮箱加密
        if (StringUtils.isNotBlank(record.getEmail()) && record.verifyEmail())
            record.setEmail(aesUtil.encrypt(record.getEmail()));

        // URL加密
        if (StringUtils.isNotBlank(record.getUrl()))
            record.setUrl(aesUtil.encrypt(record.getUrl()));

        // 备注信息加密
        if (StringUtils.isNotBlank(record.getRemark()))
            record.setRemark(aesUtil.encrypt(record.getRemark()));

        record.setCreateTime(new Date());
        record.setCreateBy(record.getUserId());
        record.setUpdateTime(null);
        record.setUpdateBy(null);

        System.out.println(record);
        return MrsResult.ok();
    }

}
