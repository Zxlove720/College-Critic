package a311.college.service.impl;

import a311.college.constant.message.MessageConstant;
import a311.college.constant.user.LoginErrorConstant;
import a311.college.constant.user.UserStatusConstant;
import a311.college.constant.user.UserSubjectConstant;
import a311.college.dto.login.LoginSymbol;
import a311.college.dto.login.PhoneLoginDTO;
import a311.college.dto.user.AddFavoriteDTO;
import a311.college.dto.user.CodeDTO;
import a311.college.dto.user.PasswordEditDTO;
import a311.college.dto.user.UserDTO;
import a311.college.dto.login.LoginDTO;
import a311.college.entity.user.User;
import a311.college.enumeration.ProvinceEnum;
import a311.college.exception.*;
import a311.college.mapper.college.CollegeMapper;
import a311.college.mapper.user.UserMapper;
import a311.college.redis.RedisKeyConstant;
import a311.college.regex.RegexUtils;
import a311.college.result.LoginResult;
import a311.college.result.Result;
import a311.college.service.UserService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.UserVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Year;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户相关服务
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final CollegeMapper collegeMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, CollegeMapper collegeMapper) {
        this.userMapper = userMapper;
        this.collegeMapper = collegeMapper;
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录：手机号 + 密码
     *
     * @param loginDTO 封装用户登录数据的DTO
     * @return User用户对象
     */
    @Override
    public LoginResult login(LoginDTO loginDTO) {
        // 1.获取用户的手机号和密码
        String phone = loginDTO.getPhone();
        String password = loginDTO.getPassword();
        // 2.根据手机号查询用户
        User user = userMapper.selectByPhone(phone);
        // 3.处理异常情况
        if (user == null) {
            // 3.1账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 3.2密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            // 3.3密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        // 3.4判断当前用户是否可用
        if (user.getStatus().equals(UserStatusConstant.DISABLE)) {
            // 3.5账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        // 4.用户正常，返回登录成功结果
        return loginSuccessful(user);
    }

    /**
     * 用户登录成功，返回成功结果
     *
     * @param user 用户
     * @return LoginResult
     */
    private LoginResult loginSuccessful(User user) {
        LoginResult result = new LoginResult();
        result.setUuid(saveUserInRedis(user));
        result.setHead(user.getHead());
        result.setUsername(user.getUsername());
        result.setPhone(user.getPhone());
        result.setPassword(user.getPassword());
        result.setProvince(user.getProvince());
        result.setSubjects(user.getSubjects());
        result.setGrade(user.getGrade());
        return result;
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
     * 发送验证码登录
     *
     * @param codeDTO 验证码DTO
     * @return code 验证码
     */
    @Override
    public String sendCode(CodeDTO codeDTO) {
        return code(codeDTO.getPhone(), RedisKeyConstant.USER_CODE_KEY);
    }

    /**
     * 发送验证码修改密码
     *
     * @param codeDTO 验证码DTO
     * @return code 验证码
     */
    @Override
    public String sendEditCode(CodeDTO codeDTO) {
        return code(codeDTO.getPhone(), RedisKeyConstant.USER_EDIT_CODE_KEY);
    }

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param preKey 键前缀
     * @return code 验证码
     */
    private String code(String phone, String preKey) {
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果手机号不合法，返回错误信息
            return LoginErrorConstant.PHONE_NUMBER_ERROR;
        }
        // 3.手机号合法，生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 4.将验证码保存至redis
        stringRedisTemplate.opsForValue().set(preKey + phone, code);
        stringRedisTemplate.expire(preKey + phone,
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
    public LoginResult phoneLogin(PhoneLoginDTO phoneLoginDTO) {
        // 1.校验手机号是否合法
        String phone = phoneLoginDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 1.1手机号不合法直接抛出异常
            throw new LoginFailedException(LoginErrorConstant.PHONE_NUMBER_ERROR);
        }
        // 2.校验验证码
        // 2.1获取redis中存储的验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisKeyConstant.USER_CODE_KEY + phone);
        // 2.2获取请求中的验证码
        String code = phoneLoginDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 2.3验证码比对失败直接抛出异常
            throw new LoginFailedException(LoginErrorConstant.CODE_ERROR);
        }
        // 3.验证码校验成功，通过手机号查询用户
        User user = userMapper.selectByPhone(phone);
        // 3.1判断用户是否存在
        if (user == null) {
            // 3.2用户不存在，直接先临时注册
            log.info(LoginErrorConstant.NO_REGISTER_PHONE);
            registerTempUser(phone);
            User tempUser = userMapper.selectByPhone(phone);
            // 3.3用户自动登录，将其登录信息存入redis
            String token = saveUserInRedis(tempUser);
        }
        // 4.用户存在，返回用户登录成功结果
        return loginSuccessful(user);
    }

    /**
     * 用户手机号未注册，自动注册
     */
    private void registerTempUser(String phone) {
        User user = new User();
        user.setUsername("用户" + RandomUtil.randomString(10));
        user.setPassword(DigestUtil.md5Hex("123456".getBytes()));
        user.setPhone(phone);
        user.setHead("https://123456.com");
        user.setYear(Year.now());
        user.setProvince(ProvinceEnum.重庆);
        user.setPattern(1);
        user.setSubjects(UserSubjectConstant.SCIENCE);
        user.setGrade(520);
        user.setRanking(50000);
        userMapper.register(user);
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return User
     */
    @Override
    public UserVO selectById(Long id) {
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
    public void register(UserDTO userDTO) {
        // DTO是方便接收前端传递的用户信息，但是在数据库中存储的用户信息需要在DTO上进行额外的扩展，需要将DTO封装为实体对象
        // 因为DTO和实体对象中的属性高度相似，所以说直接使用BeanUtils中的copyProperties方法进行对象属性拷贝即可
        User user = new User();
        // 对象属性拷贝
        BeanUtils.copyProperties(userDTO, user);
        // 根据用户所在省份确定高考模式（老高考和新高考）
        if (user.getProvince().getStatus() == 0) {
            // 确定用户为老高考
            user.setPattern(0);
            // 确定老高考用户文理科
            if (user.getCategory() == 1) {
                // 确定用户为理科，为其添加选科
                user.setSubjects(UserSubjectConstant.SCIENCE);
            } else if (user.getCategory() == 0) {
                // 确定用户为文科，为其添加选科
                user.setSubjects(UserSubjectConstant.ART);
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
     * 用户修改密码
     *
     * @param passwordEditDTO 修改密码DTO
     * @return LoginResult
     */
    @Override
    public LoginResult editPassword(PasswordEditDTO passwordEditDTO) {
        String phone = passwordEditDTO.getPhone();
        String code = passwordEditDTO.getCode();
        // 1.判断手机号是否合法
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 1.1.如果手机号不合法，返回错误信息
            throw new PasswordEditFailedException(LoginErrorConstant.PHONE_NUMBER_ERROR);
        }
        // 2.获取redis中验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisKeyConstant.USER_EDIT_CODE_KEY + phone);
        if (!code.equals(cacheCode)) {
            // 2.1验证码不匹配，修改密码失败，抛出异常
            throw new PasswordEditFailedException(LoginErrorConstant.CODE_ERROR);
        }
        // 3.手机号和验证码比对成功，可以修改密码
        userMapper.editPassword(DigestUtil.md5Hex(passwordEditDTO.getNewPassword().getBytes()), phone);
        return loginSuccessful(userMapper.selectByPhone(phone));
    }


    @Override
    public void addFavorite(AddFavoriteDTO addFavoriteDTO) {
        String table = userMapper.selectFavoriteById(addFavoriteDTO.getId());
        table = table + "," + addFavoriteDTO.getSchoolId();
        userMapper.addFavoriteTable(table, addFavoriteDTO.getId());
    }

    @Override
    public List<CollegeSimpleVO> showFavorite() {
        Long id = ThreadLocalUtil.getCurrentId();
        String favoriteTable = userMapper.selectFavoriteById(id);
        String[] favorite = favoriteTable.split(",");
        List<CollegeSimpleVO> collegeList = new ArrayList<>();
        for (String school : favorite) {
            collegeList.add(collegeMapper.selectBySchoolId(school));
        }
        return collegeList;
    }
}
