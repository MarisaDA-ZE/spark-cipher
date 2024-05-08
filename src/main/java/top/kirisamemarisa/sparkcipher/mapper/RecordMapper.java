package top.kirisamemarisa.sparkcipher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.kirisamemarisa.sparkcipher.entity.Record;

/**
 * @Author Marisa
 * @Description 记录服务mapper
 * @Date 2023/11/28
 **/
@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    /**
     * 按ID更新记录
     *
     * @param record 记录
     * @return 结果
     */
    boolean updateRecordById(Record record);
}
