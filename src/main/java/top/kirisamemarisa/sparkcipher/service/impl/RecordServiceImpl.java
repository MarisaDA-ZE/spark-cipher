package top.kirisamemarisa.sparkcipher.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.entity.Record;
import top.kirisamemarisa.sparkcipher.entity.RecordItem;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.entity.vo.RecordVo;
import top.kirisamemarisa.sparkcipher.mapper.RecordMapper;
import top.kirisamemarisa.sparkcipher.service.IRecordService;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author Marisa
 * @Description 记录接口实现类
 * @Date 2023/11/28
 **/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Resource
    private SecurityUtils securityUtils;

    @Override
    public boolean updateRecordById(RecordVo recordVo) {
        String rid = recordVo.getId();
        User authUser = securityUtils.getAuthUser();
        String uid = authUser.getId();
        System.out.println("ID: " + recordVo.getUserId() + ", " + uid);
        if (!(recordVo.getUserId() + "").equals(uid)) return false;

        Record dbRecord = baseMapper.selectById(rid);
        if (ObjectUtils.isEmpty(dbRecord)) return false;

        RecordItem title = recordVo.getTitle();
        String value = title.getValue();
        Record record = recordVo.toDto();
        record.setSearchText(value);
        record.encryptField();
        record.setId(dbRecord.getId());

        // 设置更新时间
        record.setUpdateBy(authUser.getId());
        record.setUpdateTime(new Date());
        // System.out.println("更新前: " + record);
        return baseMapper.updateRecordById(record);
    }

}
