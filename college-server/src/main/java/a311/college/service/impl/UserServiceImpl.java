package a311.college.service.impl;

import a311.college.constant.MessageConstant;
import a311.college.constant.UserStatusConstant;
import a311.college.dao.UserLoginDTO;
import a311.college.entity.User;
import a311.college.exception.AccountLockedException;
import a311.college.exception.AccountNotFoundException;
import a311.college.exception.PasswordErrorException;
import a311.college.mapper.UserMapper;
import a311.college.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户相关服务
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl (UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户登录
     * @param userLoginDTO 封装用户登录数据的DTO
     * @return User用户对象
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        // 根据用户名查询数据库中数据
        User user = userMapper.getUserByUsername(username);

        // 处理异常情况
        if (user == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (user.getStatus().equals(UserStatusConstant.DISABLE)) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 此时登录成功，可以返回
        return user;
    }
}
