package top.kirisamemarisa.sparkcipher.config;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.exception.NotFoundException;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;

/**
 * @Author Marisa
 * @Description 全局异常处理
 * @Date 2022/11/10
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求要求用户的身份认证
     * <p>401身份认证失败</p>
     * @param e 异常名称
     * @return .
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MrsResult<?> unauthorizedException(UnauthorizedException e) {
        return MrsResult.failed(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    /**
     * 资源不存在
     * <p>404资源未找到</p>
     * @param e .
     * @return .
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MrsResult<?> userNotExistException(NotFoundException e) {
        return MrsResult.failed(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

//    /**
//     * 权限不足
//     *
//     * @param e .
//     * @return .
//     */
//    @ExceptionHandler(ForbiddenException.class)
//    public MrsResult<?> forbiddenException(ForbiddenException e) {
//        return MrsResult.failed(403, e.getMessage());
//    }
//
//    /**
//     * 重复登录
//     *
//     * @param e .
//     * @return .
//     */
//    @ExceptionHandler(LoggedAccountException.class)
//    public MrsResult<?> loggedAccountException(LoggedAccountException e) {
//        return MrsResult.failed(420, "账户异地登陆", e.getMessage());
//    }
//
//    /**
//     * 登录错误
//     *
//     * @param e .
//     * @return .
//     */
//    @ExceptionHandler(LoginFailedException.class)
//    public MrsResult<?> loginFailedException(LoginFailedException e) {
//        return MrsResult.failed(421, e.getMessage());
//    }
//
//    /**
//     * 校验失败
//     *
//     * @param e .
//     * @return .
//     */
//    @ExceptionHandler(VerifyException.class)
//    public MrsResult<?> verifyFailedException(VerifyException e) {
//        return MrsResult.failed(403, e.getMessage());
//    }
//
//    /**
//     * 数据解密异常处理
//     *
//     * @param e .
//     * @return .
//     */
//    @ExceptionHandler(DecryptException.class)
//    public MrsResult<?> decryptException(DecryptException e) {
//        return MrsResult.failed(e.getMessage());
//    }
//
//    /**
//     * 数据加密异常处理
//     *
//     * @param e .
//     * @return .
//     */
//    @ExceptionHandler(EncryptException.class)
//    public MrsResult<?> encryptException(EncryptException e) {
//        return MrsResult.failed(e.getMessage());
//    }
//
//    /**
//     * 文件上传异常
//     *
//     * @param e .
//     * @return .
//     */
//    @ExceptionHandler(FileUploadException.class)
//    public MrsResult<?> fileUploadException(FileUploadException e) {
//        return MrsResult.failed(e.getMessage());
//    }
}
