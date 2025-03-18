package a311.college.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果Result
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "统一返回结果")
public class Result<T> implements Serializable {

    @Schema(description = "请求状态(HTTP请求通用状态码)")
    private String status;

    @Schema(description = "编码：1.成功；0和其他数字都是失败")
    private Integer code;

    @Schema(description = "返回信息")
    private String msg;

    @Schema(description = "返回数据")
    private T responseData;

    /**
     * 统一的不带数据的成功响应
     * @return Result响应结果
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = "successful";
        return result;
    }

    /**
     * 统一的带数据的成功响应
     * @param data 响应数据
     * @return Result响应结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = "successful";
        result.responseData = data;
        return result;
    }

    /**
     * 统一的不带数据的失败响应
     * @param msg 错误信息
     * @return Result响应结果
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.status = msg.split("-")[1];
        result.code = 0;
        result.msg = msg;
        return result;
    }

    /**
     * 统一的带数据的失败响应
     * @param msg 错误信息
     * @param data 响应数据
     * @return Result响应结果
     */
    public static <T> Result<T> error(String msg, T data) {
        Result<T> result = new Result<>();
        result.status = msg.split("-")[1];
        result.code = 0;
        result.msg = msg;
        result.responseData = data;
        return result;
    }
}
