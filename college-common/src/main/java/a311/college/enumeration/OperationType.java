package a311.college.enumeration;

// 使用枚举定义数据库操作，便于AOP进行自动装填

/**
 * 数据库操作类型
 */
public enum OperationType {

    /**
     * 更新操作
     */
    UPDATE,

    /**
     * 插入操作
     */
    INSERT
}
