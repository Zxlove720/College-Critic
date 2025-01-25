package a311.college.exception;

/**
 * 用户未登录异常
 */
public class UserNotLoginException extends BaseException{

    public UserNotLoginException(String msg) {
        super(msg);
    }

}
