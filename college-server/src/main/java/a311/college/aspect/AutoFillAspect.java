package a311.college.aspect;

import a311.college.annotation.AutoFill;
import a311.college.constant.AutoFillConstant;
import a311.college.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 通过AOP技术填充公共字段
 */

@Component
@Slf4j
@Aspect
public class AutoFillAspect {

    /**
     * 切入点
     * 切入点表达式为：在a311.college.mapper中所有有@AutoFill的方法
     */
    @Pointcut("execution(* a311.college.mapper.*.*(..)) && @annotation(a311.college.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 自动填充公共字段
     * @param joinPoint 连接点
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("公共字段自动填充...");
        // 获取方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的注解对象
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        // 获取数据库操作类型
        OperationType operationType = annotation.value();
        // 获取当前被拦截的方法参数 -- 实体对象
        Object[] args = joinPoint.getArgs();
        // 如果方法参数为null或者为空，则直接返回，无需自动填充
        if (args == null || args.length == 0) {
            return;
        }
        // 获取当前方法的实体类（也就是Object的第一个元素）
        Object entity = args[0];
        // 准备赋值数据
        LocalDateTime now = LocalDateTime.now();
        // 根据不同的操作类型，为对应的属性通过反射进行公共字段赋值
        if (operationType == OperationType.INSERT) {
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                // 通过反射赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                setUpdateTime.invoke(entity, now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
