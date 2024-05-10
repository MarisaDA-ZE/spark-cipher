package top.kirisamemarisa.sparkcipher.exception;

/**
 * @Author Marisa
 * @Description 服务器异常
 * @Date 2024/5/10
 */
public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException(String msg) {
        super(msg);
    }
}
