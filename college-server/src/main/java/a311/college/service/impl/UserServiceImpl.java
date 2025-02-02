package a311.college.service.impl;

import a311.college.constant.MessageConstant;
import a311.college.constant.UserStatusConstant;
import a311.college.dao.PasswordEditDTO;
import a311.college.dao.UserDTO;
import a311.college.dao.UserLoginDTO;
import a311.college.dao.UserPageQueryDTO;
import a311.college.entity.User;
import a311.college.exception.AccountLockedException;
import a311.college.exception.AccountNotFoundException;
import a311.college.exception.PasswordEditFailedException;
import a311.college.exception.PasswordErrorException;
import a311.college.mapper.UserMapper;
import a311.college.result.PageResult;
import a311.college.service.UserService;
import a311.college.thread.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

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
        // 判断当前用户是否可用
        if (user.getStatus().equals(UserStatusConstant.DISABLE)) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        // 此时登录成功，可以返回
        return user;
    }

    /**
     * 用户分页查询
     * @param userPageQueryDTO 用户分页查询DTO
     * @return PageResult<User>
     */
    @Override
    public PageResult<User> pageSelect(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        // PageHelper默认返回Page对象，Page是继承了ArrayList的，其中存储的是查询到的对象
        Page<User> page = userMapper.pageQuery(userPageQueryDTO);
        // 获取总页数
        long total = page.getTotal();
        // 获取每一页的记录
        List<User> record = page.getResult();
        return new PageResult<>(total, record);
    }

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return User
     */
    @Override
    public User selectById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 删除用户（用户注销）
     * @param id 用户id
     */
    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    /**
     * 新增用户（用户注册）
     * @param userDTO 用户DTO
     */
    @Override
    public void save(UserDTO userDTO) {
        // DTO是方便接收前端传递的用户信息，但是在数据库中存储的用户信息需要在DTO上进行额外的扩展，需要将DTO封装为实体对象
        // 因为DTO和实体对象中的属性高度相似，所以说直接使用BeanUtils中的copyProperties方法进行对象属性拷贝即可
        User user = new User();
        // 对象属性拷贝
        BeanUtils.copyProperties(userDTO, user);
        // 将拷贝后的对象属性补全
        user.setStatus(UserStatusConstant.ENABLE);
        // 将用户密码加密后存储
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        // 使用AOP将创建、操作时间补全

        userMapper.insert(user);
    }

    /**
     * 修改用户信息
     * @param userDTO 用户DTO
     */
    @Override
    public void update(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userMapper.update(user);
    }

    /**
     * 修改用户状态
     * @param status 用户当前状态
     * @param id 用户id
     */
    @Override
    public void changeStatus(Integer status, Long id) {
        // 不能给mapper直接传递status和id，需要将其封装为User对象之后再传递
        User user = User.builder()
                .status(status)
                .id(id)
                .build();
        userMapper.update(user);
    }

    /**
     * 用户修改密码
     * @param passwordEditDTO 用户密码修改DTO
     */
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        // 获取当前用户id
        Long id = ThreadLocalUtil.getCurrentId();
        log.warn("id为{}的用户正在修改密码", id);
        User user = userMapper.selectById(id);
        // 比对用户提供的旧密码是否和数据库中真实的旧密码一致，如果一致，则可以 修改密码
        if (DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()).equals(user.getPassword())) {
            // 当前用户输入的旧密码和数据库中的旧密码一致，可以修改
            // 用户输入的新密码是没有加密的，所以说需要先进行加密，再存入数据库
            user.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
            userMapper.update(user);
        } else {
            // 比对失败，抛出异常
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
    }
}
