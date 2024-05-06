package top.kirisamemarisa.sparkcipher.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.kirisamemarisa.sparkcipher.entity.UserVo;

/**
 * @Author Marisa
 * @Description MrsLResp（MrsLoginResponse）登录返回对象
 * @Date 2024/5/6
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MrsLResp {
    UserVo userVo;
    String token;
}
