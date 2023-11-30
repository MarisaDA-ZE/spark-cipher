package top.kirisamemarisa.sparkcipher.exception;


/**
 * @author Marisa
 * @Description 资源未找到
 * @Date 2022/11/10
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
