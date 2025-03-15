package a311.college.service.impl;

import a311.college.constant.user.LoginErrorConstant;
import a311.college.constant.user.UserSubjectConstant;
import a311.college.dto.login.LoginSymbol;
import a311.college.dto.user.*;
import a311.college.dto.login.LoginDTO;
import a311.college.entity.user.User;
import a311.college.exception.*;
import a311.college.mapper.college.CollegeMapper;
import a311.college.mapper.user.UserMapper;
import a311.college.redis.RedisKeyConstant;
import a311.college.regex.RegexUtils;
import a311.college.result.LoginResult;
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
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 1.1手机号格式不合法，登录失败
            throw new LoginFailedException(LoginErrorConstant.PHONE_NUMBER_ERROR);
        }
        // 2.根据手机号查询用户
        User user = userMapper.selectByPhone(phone);
        // 3.处理异常情况
        if (user == null) {
            // 3.1手机号不存在，登录失败
            throw new LoginFailedException(LoginErrorConstant.ACCOUNT_NOT_FOUND);
        }
        // 3.2密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            // 3.3密码错误，登录失败
            throw new LoginFailedException(LoginErrorConstant.PASSWORD_ERROR);
        }
        // 3.4判断当前用户是否可用
        if (user.getStatus().equals(LoginErrorConstant.DISABLE)) {
            // 3.5账号被锁定，登录失败
            throw new LoginFailedException(LoginErrorConstant.ACCOUNT_LOCKED);
        }
        // 4.用户正常，成功登录，返回登录成功结果
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
        // 将用户的登录凭证保存至redis
        result.setUuid(saveUserInRedis(user));
        // 封装登录成功结果
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
        // 2.1判断用户是否在缓存中已经存在登录凭证
        String oldToken = stringRedisTemplate.opsForValue().get(RedisKeyConstant.USER_LOGIN_KEY + id);
        if (oldToken != null) {
            // 2.2存在登录凭证，此时用户已经登录，删除原登录凭证
            stringRedisTemplate.delete(oldToken);
            // 2.3删除用户id和token的映射关系
            stringRedisTemplate.delete(RedisKeyConstant.USER_LOGIN_KEY + id);
            // 2.4此时相当于用户原有的登录已经退出
        }
        // 3重新登录
        // 3.1生成新的登录凭据
        String token = UUID.randomUUID().toString(true);
        String loginKey = RedisKeyConstant.USER_KEY + token;
        // 3.2将用户登录信息缓存到redis
        LoginSymbol userPhoneLoginDTO = new LoginSymbol();
        BeanUtil.copyProperties(user, userPhoneLoginDTO);
        // 3.3将DTO类中的属性转换为String
        Map<String, Object> userMap = BeanUtil.beanToMap(userPhoneLoginDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(loginKey, userMap);
        stringRedisTemplate.expire(loginKey, RedisKeyConstant.USER_TTL, TimeUnit.SECONDS);
        // 4.新增用户和其登录凭据的映射关系
        stringRedisTemplate.opsForValue().set(
                RedisKeyConstant.USER_LOGIN_KEY + id,
                loginKey,
                RedisKeyConstant.USER_TTL,
                TimeUnit.SECONDS
        );
        return token;
    }

    /**
     * 用户发送验证码注册
     *
     * @param codeDTO 验证码DTO
     * @return code 验证码
     */
    @Override
    public String sendRegisterCode(CodeDTO codeDTO) {
        return code(codeDTO.getPhone(), RedisKeyConstant.USER_REGISTER_CODE_KEY);
    }

    /**
     * 用户注册
     *
     * @param userDTO userDTO
     */
    @Override
    public void register(UserDTO userDTO) {
        // 0.获取验证码比对
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisKeyConstant.USER_REGISTER_CODE_KEY);
        if (!userDTO.getCode().equals(cacheCode)) {
            // 0.验证码错误，注册失败
            throw new LoginFailedException(LoginErrorConstant.CODE_ERROR);
        }
        // 1.验证码比对成功,将用户DTO封装为用户实体对象
        User user = new User();
        // 1.1进行属性拷贝
        BeanUtil.copyProperties(userDTO, user);
        // 1.2将用户密码进行MD5加密
        user.setPassword(DigestUtil.md5Hex(user.getPassword().getBytes()));
        // 2.判断用户的高考模模式
        if (user.getPattern().equals(0)) {
            // 2.1用户为老高考，需要确定用户的文理分科
            if (user.getCategory().equals(1)) {
                // 2.2用户为理科，为其添加选科
                user.setSubjects(UserSubjectConstant.SCIENCE);
            } else {
                // 2.3用户为文科，为其添加选科
                user.setSubjects(UserSubjectConstant.ART);
            }
        }
        // 3.为新用户初始化收藏表和志愿表
        user.setFavoriteTable("");
        user.setCollegeTable("");
        userMapper.register(user);
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
     * 发送验证码进行注销
     *
     * @param codeDTO 验证码DTO
     * @return code 验证码
     */
    @Override
    public String sendDeleteCode(CodeDTO codeDTO) {
        return code(codeDTO.getPhone(), RedisKeyConstant.USER_DELETE_CODE_KEY);
    }

    /**
     * 发送验证码
     *
     * @param phone  手机号
     * @param preKey 键前缀
     * @return code 验证码
     */
    private String code(String phone, String preKey) {
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果手机号不合法，返回错误信息
            throw new LoginFailedException(LoginErrorConstant.PHONE_NUMBER_ERROR);
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

    /**
     * 用户登出
     *
     * @param layoutDTO 登出DTO
     */
    @Override
    public void layout(LayoutDTO layoutDTO) {
        // 删除redis中的用户登录信息
        stringRedisTemplate.delete(RedisKeyConstant.USER_KEY + layoutDTO.getPhone());
        // 删除redis中的用户登录凭证
        stringRedisTemplate.delete(RedisKeyConstant.USER_LOGIN_KEY + ThreadLocalUtil.getCurrentId());
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
     * 用户收藏学校
     *
     * @param addFavoriteDTO 学校收藏DTO
     */
    @Override
    public void addFavorite(AddFavoriteDTO addFavoriteDTO) {
        String table = userMapper.selectFavoriteById(ThreadLocalUtil.getCurrentId());
        table = table + "," + addFavoriteDTO.getSchoolId();
        userMapper.addFavoriteTable(table, ThreadLocalUtil.getCurrentId());
    }

    /**
     * 展示用户收藏
     *
     * @return List<CollegeSimpleVO>
     */
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

    /**
     * 修改用户信息
     *
     * @param userDTO 用户DTO
     */
    @Override
    public void update(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(ThreadLocalUtil.getCurrentId());
        userMapper.update(user);
    }

    /**
     * 用户注销
     *
     * @param deleteDTO 用户注销DTO
     */
    @Override
    public void deleteUser(DeleteDTO deleteDTO) {
        // 1.获取redis中缓存的验证码
        // 1.1获取用户手机号和验证码
        String phone = deleteDTO.getPhone();
        String code = deleteDTO.getCode();
        // 1.2获取当前用户id
        long id = ThreadLocalUtil.getCurrentId();
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisKeyConstant.USER_DELETE_CODE_KEY + phone);
        // 2.进行验证码比对
        if (!code.equals(cacheCode)) {
            // 2.1验证码比对失败，注销失败
            throw new CodeErrorException(LoginErrorConstant.CODE_ERROR);
        }
        // 3.验证码比对成功，注销用户
        userMapper.deleteById(id);
        // 4.在redis中删除用户的登录信息
        stringRedisTemplate.delete(RedisKeyConstant.USER_KEY + phone);
        stringRedisTemplate.delete(RedisKeyConstant.USER_LOGIN_KEY + id);
    }

    /**
     * 查看用户评论
     *
     * @return List<String> 用户评论列表
     */
    @Override
    public List<String> showComment() {
        return userMapper.selectComment(ThreadLocalUtil.getCurrentId());
    }

}
