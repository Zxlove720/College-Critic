package a311.college.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadLocal工具类
 */
@Slf4j
public class ThreadLocalUtil {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    // 设置当前线程局部变量的值
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    // 获取当前线程的局部变量的值
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    // 清空当前线程的局部变量的值
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
