package a311.college.service.impl;

import a311.college.constant.message.MessageConstant;
import a311.college.constant.user.LoginErrorConstant;
import a311.college.constant.user.UserStatusConstant;
import a311.college.dto.login.LoginSymbol;
import a311.college.dto.login.PhoneLoginDTO;
import a311.college.dto.user.PasswordEditDTO;
import a311.college.dto.user.UserDTO;
import a311.college.dto.login.LoginDTO;
import a311.college.dto.user.UserPageQueryDTO;
import a311.college.entity.user.User;
import a311.college.exception.AccountLockedException;
import a311.college.exception.AccountNotFoundException;
import a311.college.exception.PasswordEditFailedException;
import a311.college.exception.PasswordErrorException;
import a311.college.mapper.user.UserMapper;
import a311.college.redis.RedisKeyConstant;
import a311.college.regex.RegexUtils;
import a311.college.result.PageResult;
import a311.college.service.UserService;
import a311.college.thread.ThreadLocalUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户相关服务
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录：用户名 + 密码
     *
     * @param loginDTO 封装用户登录数据的DTO
     * @return User用户对象
     */
    @Override
    public String login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        // 根据用户名查询数据库中数据
        User user = userMapper.userLogin(username);
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
        return saveUserInRedis(user);
    }

    /**
     * 将用户的登录凭据保存到redis
     *
     * @param user 用户
     * @return token 登录凭据
     */
    private String saveUserInRedis(User user) {
        // 1.获取用户的唯一标识符作为其登录凭证的映射
        Long id = user.getId();
        // 2检查用户是否重复登录
        String oldToken = stringRedisTemplate.opsForValue().get(RedisKeyConstant.USER_LOGIN_KEY + id);
        if (oldToken != null) {
            // 2.1此时用户重复登录了，删除原登录凭据
            stringRedisTemplate.delete(oldToken);
            // 2.2删除用户id和token的映射关系
            stringRedisTemplate.delete(RedisKeyConstant.USER_LOGIN_KEY + id);
            // 此时相当于用户原有的登录已经退出
        }
        // 3重新登录
        // 3.1生成新的登录凭据
        String token = UUID.randomUUID().toString(true);
        String tokenKey = RedisKeyConstant.USER_KEY + token;
        // 3.2将用户登录信息缓存到redis
        LoginSymbol userPhoneLoginDTO = new LoginSymbol();
        BeanUtil.copyProperties(user, userPhoneLoginDTO);
        // 3.3将DTO类中的属性转换为String
        Map<String, Object> userMap = BeanUtil.beanToMap(userPhoneLoginDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, RedisKeyConstant.USER_TTL, TimeUnit.SECONDS);
        // 4.新增用户和其登录凭据的映射关系
        stringRedisTemplate.opsForValue().set(
                RedisKeyConstant.USER_LOGIN_KEY + id,
                tokenKey,
                RedisKeyConstant.USER_TTL,
                TimeUnit.SECONDS
        );
        return token;
    }

    /**
     * 发送验证码
     *
     * @param phone 用户手机号
     * @return Result<String>
     */
    @Override
    public String sendCode(String phone) {
        // 1.校验手机号是否合法
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果手机号不合法，返回错误信息
            return LoginErrorConstant.PHONE_NUMBER_ERROR;
        }
        // 3.手机号合法，生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 4.将验证码保存至redis
        stringRedisTemplate.opsForValue().set(RedisKeyConstant.USER_CODE_KEY + phone, code);
        stringRedisTemplate.expire(RedisKeyConstant.USER_CODE_KEY + phone,
                RedisKeyConstant.USER_CODE_TTL, TimeUnit.SECONDS);
        // 5.发送验证码（短信功能待完成）
        //TODO 后期如果有机会可以将其改为真实的发送手机验证码
        log.info("发送短信验证码成功，验证码为：{}", code);
        log.info(RedisKeyConstant.CODE_TIME_MESSAGE);
        // 6.响应结果
        return code;
    }

    /**
     * 手机登录
     *
     * @param phoneLoginDTO 手机登录DTO
     * @return String token
     */
    @Override
    public String phoneLogin(PhoneLoginDTO phoneLoginDTO) {
        // 1.校验手机号是否合法
        String phone = phoneLoginDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 1.1手机号不合法直接返回错误
            return LoginErrorConstant.PHONE_NUMBER_ERROR;
        }
        // 2.校验验证码
        // 2.1获取redis中存储的验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisKeyConstant.USER_CODE_KEY + phone);
        // 2.2获取请求中的验证码
        String code = phoneLoginDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 2.3验证码比对失败，返回错误
            return LoginErrorConstant.CODE_ERROR;
        }
        // 3.验证码校验成功，通过手机号查询用户
        User user = userMapper.selectByPhone(phone);
        // 3.1判断用户是否存在
        if (user == null) {
            // 3.2用户不存在，直接返回
            log.info(LoginErrorConstant.NO_REGISTER_PHONE + "自动注册");
            User registerUser = new User();
            registerUser.setPhone(phone);
            registerUser.setNickname("用户" + RandomUtil.randomString(10));
            userMapper.register(registerUser);
            return UUID.randomUUID().toString(true);
        }
        // 4.用户存在，那么将用户的登录信息存储在redis
        // 4.1随机生成token，作为登录令牌
        return saveUserInRedis(user);
    }

    /**
     * 用户分页查询
     *
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
     *
     * @param id 用户id
     * @return User
     */
    @Override
    public User selectById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 删除用户（用户注销）
     *
     * @param id 用户id
     */
    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    /**
     * 新增用户（用户注册）
     *
     * @param userDTO 用户DTO
     */
    @Override
    public void save(UserDTO userDTO) {
        // DTO是方便接收前端传递的用户信息，但是在数据库中存储的用户信息需要在DTO上进行额外的扩展，需要将DTO封装为实体对象
        // 因为DTO和实体对象中的属性高度相似，所以说直接使用BeanUtils中的copyProperties方法进行对象属性拷贝即可
        User user = new User();
        // 对象属性拷贝
        BeanUtils.copyProperties(userDTO, user);
        // 根据用户所在省份确定高考模式
        if (user.getProvince().getStatus() == 0) {
            // 确定用户为老高考
            user.setPattern(0);
            List<String> subjects = new ArrayList<>();
            // 确定老高考用户文理科
            if (user.getCategory() == 1) {
                // 确定用户为理科，为其添加选科
                Collections.addAll(subjects, "物理", "化学", "生物");
                user.setSubjects(subjects.toString());
            } else if (user.getCategory() == 0) {
                // 确定用户为文科，为其添加选科
                Collections.addAll(subjects, "历史", "政治", "地理");
                user.setSubjects(subjects.toString());
            }
        }
        // 将拷贝后的对象属性补全
        user.setStatus(UserStatusConstant.ENABLE);
        // 将用户密码加密后存储
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        // 使用AOP将创建、操作时间补全
        userMapper.insert(user);
    }

    /**
     * 修改用户信息
     *
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
     *
     * @param status 用户当前状态
     * @param id     用户id
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
     *
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
