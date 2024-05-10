package top.kirisamemarisa.sparkcipher.exception;

/**
 * @Author Marisa
 * @Description 资源不可访问
 * @Date 2024/5/10
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String msg) {
        super(msg);
    }
}
