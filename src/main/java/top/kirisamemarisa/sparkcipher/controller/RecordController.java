package top.kirisamemarisa.sparkcipher.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.aop.SM2Crypto;
import top.kirisamemarisa.sparkcipher.aop.enums.CRYPTO_TYPE;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.Record;
import top.kirisamemarisa.sparkcipher.entity.RecordItem;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.entity.vo.RecordVo;
import top.kirisamemarisa.sparkcipher.service.IRecordService;
import top.kirisamemarisa.sparkcipher.util.encrypto.aes.AES256Utils;
import top.kirisamemarisa.sparkcipher.util.IdUtils;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author Marisa
 * @Description 用户服务控制层
 * @Date 2023/11/27
 **/
@CrossOrigin
@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    private IRecordService recordService;


    @Resource
    private SecurityUtils securityUtils;

    @GetMapping("/test")
    public MrsResult<?> test() {
        String s = "原神,启动！！！！";
        String encrypt = AES256Utils.encrypt(s);
        System.out.println(encrypt);
        String decrypt = AES256Utils.decrypt(encrypt);
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
    @SM2Crypto(CRYPTO_TYPE.ENCRYPT)
    @GetMapping("/getRecordsList")
    public MrsResult<?> getRecordsList(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(name = "keyWords", defaultValue = "") String text) {
        System.out.println(current + ", " + size + ", " + text);
        User authUser = securityUtils.getAuthUser();
        System.out.println(authUser);
        String uid = authUser.getId();
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID", uid);
        if (StringUtils.isNotBlank(text)) queryWrapper.like("SEARCH_TEXT", text);
        IPage<Record> dbPage = recordService.page(new Page<>(current, size), queryWrapper);
        dbPage.getRecords().forEach(Record::decryptField);
        IPage<RecordVo> page = dbPage.convert(Record::toVo);
        page.getRecords().forEach(System.out::println);
        return MrsResult.ok(page);
    }

    @GetMapping("/getRecordById")
    public MrsResult<?> getRecordById(@RequestParam(name = "id") String id) {
        User authUser = securityUtils.getAuthUser();
        System.out.println(authUser);
        String uid = authUser.getId();
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID", uid);
        queryWrapper.eq("ID", id);
        Record record = recordService.getOne(queryWrapper);
        record.decryptField();
        System.out.println("查询一个: " + record);
        return MrsResult.ok(record.toVo());
    }

    /**
     * 添加一条记录
     *
     * @param recordVo .
     * @return .
     */
    @PutMapping("/add")
    public MrsResult<?> add(@RequestBody RecordVo recordVo) {
        System.out.println(recordVo);
        if (ObjectUtils.isEmpty(recordVo)) return MrsResult.failed("对象为空!");
        User authUser = securityUtils.getAuthUser();
        String uid = authUser.getId();
        if (StringUtils.isBlank(uid)) return MrsResult.failed("用户不存在");

        Record record = recordVo.toDto();
        RecordItem title = recordVo.getTitle();
        String value = title.getValue();

        // 设置ID
        String snowflakeId = IdUtils.nextIdOne();
        record.setId(snowflakeId);
        record.setUserId(uid);
        record.setSearchText(value);
        record.setCreateTime(new Date());
        record.setCreateBy(uid);
        record.setUpdateTime(null);
        record.setUpdateBy(null);
        System.out.println("加密前: " + record);
        record.encryptField();
        System.out.println("加密后: " + record);
        boolean save = recordService.save(record);
        return save ? MrsResult.ok() : MrsResult.failed();
    }

    /**
     * 编辑
     *
     * @param recordVo .
     * @return .
     */
    @PostMapping("/edit")
    public MrsResult<?> edit(@RequestBody RecordVo recordVo) {
        System.out.println(recordVo);
        boolean isUpdate = recordService.updateRecordById(recordVo);
        return isUpdate ? MrsResult.ok() : MrsResult.failed("更新失败！");
    }

    /**
     * 删除一条记录
     *
     * @param rid .
     * @return .
     */
    @DeleteMapping("/deleteById/{rid}")
    public MrsResult<?> delete(@PathVariable String rid) {
        Record dbRecord = recordService.getById(rid);
        if (ObjectUtils.isEmpty(dbRecord)) return MrsResult.failed("记录不存在！");
        User authUser = securityUtils.getAuthUser();
        if (!(dbRecord.getUserId() + "").equals(authUser.getId()))
            return MrsResult.failed("此记录不属于您！");
        boolean isRemove = recordService.removeById(rid);
        return isRemove ? MrsResult.ok() : MrsResult.failed("删除失败！");
    }

}
