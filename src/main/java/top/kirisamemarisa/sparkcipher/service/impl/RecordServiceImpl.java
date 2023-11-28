package top.kirisamemarisa.sparkcipher.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.entity.Record;
import top.kirisamemarisa.sparkcipher.mapper.RecordMapper;
import top.kirisamemarisa.sparkcipher.service.IRecordService;

/**
 * @Author Marisa
 * @Description 记录接口实现类
 * @Date 2023/11/28
 **/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

}
