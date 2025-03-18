package a311.college.handler;


import a311.college.constant.user.UserErrorConstant;
import a311.college.exception.BaseException;
import a311.college.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param exception 业务异常
     * @return Result<Void>
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(BaseException exception) {
        log.info("异常信息：{}", exception.getMessage());
        return Result.error(exception.getMessage());
    }

    /**
     * 处理SQL异常
     * @param exception SQL异常
     * @return Result<Void>
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        // 获得报错信息
        String message = exception.getMessage();
        // 解决重复添加问题
        if (message.contains("Duplicate entry")) {
            // 按照空格对报错信息进行切片处理，获取到重复添加的值
            String[] split = message.split(" ");
            // 获取重复添加的用户名
            String username = split[2];
            // 拼接报错信息
            String errorMessage = username + UserErrorConstant.ALREADY_EXISTS;
            return Result.error(errorMessage);
        } else {
            // 并非重复添加报错
            // TODO日后可以根据需求添加更多的SQL异常处理
            return Result.error("未知错误");
        }
    }

}
