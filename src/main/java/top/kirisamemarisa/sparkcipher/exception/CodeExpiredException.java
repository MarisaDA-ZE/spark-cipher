package top.kirisamemarisa.sparkcipher.exception;

/**
 * @Author Marisa
 * @Description 验证码过期异常
 * @Date 2024/5/10
 */
public class CodeExpiredException extends RuntimeException{
    public CodeExpiredException(String msg) {
        super(msg);
    }
}
