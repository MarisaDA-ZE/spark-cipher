package top.kirisamemarisa.sparkcipher.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.Record;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.service.IRecordService;
import top.kirisamemarisa.sparkcipher.util.AES256Utils;
import top.kirisamemarisa.sparkcipher.util.IdUtils;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    private AES256Utils aesUtil;

    @Resource
    private SecurityUtils securityUtils;

    @GetMapping("/test")
    public MrsResult<?> test() {
        String s = "原神,启动！！！！";
        String encrypt = aesUtil.encrypt(s);
        System.out.println(encrypt);
        String decrypt = aesUtil.decrypt(encrypt);
        System.out.println(decrypt);
        return MrsResult.ok();
    }

    /**
     * 分页查询用户对应的记录
     *
     * @param current 当前页
     * @param size    每页长度
     * @return .
     */
    @GetMapping("/getRecordsList")
    public MrsResult<?> getRecordsList(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size) {
        User authUser = securityUtils.getAuthUser();
        String uid = authUser.getId();
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID", uid);
        IPage<Record> page = recordService.page(new Page<>(current, size), queryWrapper);
        page.getRecords().forEach(this::decryptField);
        page.getRecords().forEach(System.out::println);
        return MrsResult.ok(page);
    }

    /**
     * 根据用户ID查询记录信息
     *
     * @param userId .
     * @return .
     */
    @GetMapping("/getRecordByUserId")
    public MrsResult<?> getRecordByUserId(String userId) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID", userId);
        List<Record> list = recordService.list(queryWrapper);
        list.forEach(this::decryptField);
        list.forEach(System.out::println);
        return MrsResult.ok(list);
    }

    /**
     * 数据库层级的字段解码
     *
     * @param record .
     */
    private void decryptField(Record record) {
        // ---数据库层级的数据解密
        // 标题解密
        if (StringUtils.isNotBlank(record.getTitle()))
            record.setTitle(aesUtil.decrypt(record.getTitle()));

        // 账户名解密
        if (StringUtils.isNotBlank(record.getAccount()))
            record.setAccount(aesUtil.decrypt(record.getAccount()));

        // 用户名解密
        if (StringUtils.isNotBlank(record.getUserName()))
            record.setUserName(aesUtil.decrypt(record.getUserName()));

        // 密码解密
        if (StringUtils.isNotBlank(record.getPassword()))
            record.setPassword(aesUtil.decrypt(record.getPassword()));

        // 手机号解密
        if (StringUtils.isNotBlank(record.getPhone()))
            record.setPhone(aesUtil.decrypt(record.getPhone()));

        // 邮箱解密
        if (StringUtils.isNotBlank(record.getEmail()))
            record.setEmail(aesUtil.decrypt(record.getEmail()));

        // URL解密
        if (StringUtils.isNotBlank(record.getUrl()))
            record.setUrl(aesUtil.decrypt(record.getUrl()));

        // 备注信息解密
        if (StringUtils.isNotBlank(record.getRemark()))
            record.setRemark(aesUtil.decrypt(record.getRemark()));
        // ---到此
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
        if (ObjectUtils.isEmpty(record)) return MrsResult.failed("对象为空!");
        if (StringUtils.isBlank(record.getUserId())) return MrsResult.failed("用户ID错误!");

        // 设置ID
        String snowflakeId = IdUtils.nextIdOne();
        record.setId(snowflakeId);

        // 标题加密
        if (StringUtils.isNotBlank(record.getTitle()))
            record.setTitle(aesUtil.encrypt(record.getTitle()));
        else record.setTitle(null);

        // 账户名加密
        if (StringUtils.isNotBlank(record.getAccount()))
            record.setAccount(aesUtil.encrypt(record.getAccount()));
        else record.setAccount(null);

        // 用户名加密
        if (StringUtils.isNotBlank(record.getUserName()))
            record.setUserName(aesUtil.encrypt(record.getUserName()));
        else record.setUserName(null);

        // 密码加密
        if (StringUtils.isNotBlank(record.getPassword()))
            record.setPassword(aesUtil.encrypt(record.getPassword()));
        else record.setPassword(null);

        // 手机号加密
        if (StringUtils.isNotBlank(record.getPhone()))
            record.setPhone(aesUtil.encrypt(record.getPhone()));
        else record.setPhone(null);

        // 邮箱加密
        if (StringUtils.isNotBlank(record.getEmail()))
            record.setEmail(aesUtil.encrypt(record.getEmail()));
        else record.setEmail(null);

        // URL加密
        if (StringUtils.isNotBlank(record.getUrl()))
            record.setUrl(aesUtil.encrypt(record.getUrl()));
        else record.setUrl(null);

        // 备注信息加密
        if (StringUtils.isNotBlank(record.getRemark()))
            record.setRemark(aesUtil.encrypt(record.getRemark()));
        else record.setRemark(null);

        record.setCreateTime(new Date());
        record.setCreateBy(record.getUserId());
        record.setUpdateTime(null);
        record.setUpdateBy(null);
        System.out.println(record);

        boolean save = recordService.save(record);
        return save ? MrsResult.ok() : MrsResult.failed();
    }

}
