package top.kirisamemarisa.sparkcipher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.kirisamemarisa.sparkcipher.entity.Record;
import top.kirisamemarisa.sparkcipher.entity.vo.RecordVo;

/**
 * @Author Marisa
 * @Description 用户服务接口
 * @Date 2023/11/27
 **/
public interface IRecordService extends IService<Record> {

    /**
     * 根据ID修改记录信息
     *
     * @param recordVo 记录VO
     * @return 修改后的结果
     */
    boolean updateRecordById(RecordVo recordVo);
}
