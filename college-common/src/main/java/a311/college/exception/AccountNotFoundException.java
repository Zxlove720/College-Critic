package a311.college.exception;

/**
 * 账号不存在异常
 */
public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException(String msg) {
        super(msg);
    }

}
