package top.kirisamemarisa.sparkcipher.exception;


/**
 * @author Marisa
 * @Description 身份验证失败
 * @Date 2022/11/10
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }
}
