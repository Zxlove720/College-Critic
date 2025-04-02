package a311.college.exception;

/**
 * 用户重复收藏学校或专业
 */
public class ReAdditionException extends BaseException {
    public ReAdditionException(String message) {
        super(message);
    }
}
