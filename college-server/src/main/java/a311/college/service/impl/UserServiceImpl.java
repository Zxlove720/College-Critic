package a311.college.service.impl;

import a311.college.constant.user.UserErrorConstant;
import a311.college.constant.user.UserSubjectConstant;
import a311.college.dto.login.UserLoginSymbolDTO;
import a311.college.dto.query.major.PageQueryDTO;
import a311.college.dto.user.*;
import a311.college.dto.login.UserLoginDTO;
import a311.college.entity.major.Major;
import a311.college.entity.school.School;
import a311.college.entity.user.User;
import a311.college.exception.*;
import a311.college.mapper.user.UserMapper;
import a311.college.constant.redis.UserRedisKey;
import a311.college.regex.RegexUtils;
import a311.college.result.LoginResult;
import a311.college.result.PageResult;
import a311.college.service.UserService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.major.BriefMajorVO;
import a311.college.vo.school.BriefSchoolInfoVO;
import a311.college.vo.user.UserVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
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
     * 用户登录：手机号 + 密码
     *
     * @param userLoginDTO 封装用户登录数据的DTO
     * @return LoginResult 登录返回结果
     */
    @Override
    public LoginResult login(UserLoginDTO userLoginDTO) {
        // 1.获取用户的手机号和密码
        String phone = userLoginDTO.getPhone();
        String password = userLoginDTO.getPassword();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 1.1手机号格式不合法，登录失败
            throw new LoginFailedException(UserErrorConstant.PHONE_NUMBER_ERROR);
        }
        // 2.根据手机号查询用户
        User user = userMapper.selectByPhone(phone);
        // 3.处理异常情况
        if (user == null) {
            // 3.1手机号对应的用户不存在，登录失败
            throw new LoginFailedException(UserErrorConstant.ACCOUNT_NOT_FOUND);
        }
        // 3.2密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            // 3.3密码错误，登录失败
            throw new LoginFailedException(UserErrorConstant.PASSWORD_ERROR);
        }
        // 3.4判断当前用户是否可用
        if (user.getStatus().equals(UserErrorConstant.DISABLE)) {
            // 3.5账号被锁定，登录失败
            throw new LoginFailedException(UserErrorConstant.ACCOUNT_LOCKED);
        }
        // 4.用户正常，成功登录，返回登录成功结果
        return loginSuccessful(user);
    }

    /**
     * 用户登录成功，返回成功结果
     *
     * @param user 用户
     * @return LoginResult 登录返回结果
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
        result.setRanking(user.getRanking());
        return result;
    }

    /**
     * 将用户的登录凭据保存到redis
     *
     * @param user 用户
     * @return token 登录凭据
     */
    private String saveUserInRedis(User user) {
        String token = DigestUtil.md5Hex(UserRedisKey.USER_KEY_TOKEN + user.getId() + ":" + UserRedisKey.SECRET);
        String key = UserRedisKey.USER_KEY + token;
        Map<Object, Object> oldUserMap = stringRedisTemplate.opsForHash().entries(key);
        if (!oldUserMap.isEmpty()) {
            // 此时用户用户已经登录，需要删除登录信息，并重新登录
            stringRedisTemplate.delete(key);
        }
        // 3.2将用户登录信息缓存到redis
        UserLoginSymbolDTO loginSymbol = new UserLoginSymbolDTO();
        BeanUtil.copyProperties(user, loginSymbol);
        // 3.3将DTO类中的属性转换为String
        Map<String, Object> userMap = BeanUtil.beanToMap(loginSymbol, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(key, userMap);
        stringRedisTemplate.expire(key, UserRedisKey.USER_TTL, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 用户名检查
     *
     * @param userUsernameCheckDTO 用户名检查DTO
     * @return Integer 0该用户名不可用 1该用户名可用
     */
    @Override
    public Integer checkUser(UserUsernameCheckDTO userUsernameCheckDTO) {
        User user = userMapper.selectByUsername(userUsernameCheckDTO.getUsername());
        if (user != null) {
            // 该用户名已经存在，不能注册
            return 0;
        }
        return 1;
    }

    /**
     * 手机号检查
     *
     * @param userPhoneCheckDTO 手机号检查DTO
     * @return Integer 0该手机号不可用 1该手机号可用
     */
    @Override
    public Integer checkPhone(UserPhoneCheckDTO userPhoneCheckDTO) {
        User user = userMapper.selectByPhone(userPhoneCheckDTO.getPhone());
        if (user != null) {
            // 该手机号已经存在，不能注册
            return 0;
        }
        return 1;
    }

    /**
     * 用户注册
     *
     * @param userDTO userDTO
     */
    @Override
    public void register(UserDTO userDTO) {
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
     * @param userCodeDTO 验证码DTO
     * @return code 验证码
     */
    @Override
    public String sendEditCode(UserCodeDTO userCodeDTO) {
        return code(userCodeDTO.getPhone(), UserRedisKey.USER_EDIT_CODE_KEY);
    }

    /**
     * 发送验证码进行注销
     *
     * @param userCodeDTO 验证码DTO
     * @return code 验证码
     */
    @Override
    public String sendDeleteCode(UserCodeDTO userCodeDTO) {
        return code(userCodeDTO.getPhone(), UserRedisKey.USER_DELETE_CODE_KEY);
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
            throw new LoginFailedException(UserErrorConstant.PHONE_NUMBER_ERROR);
        }
        // 3.手机号合法，生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 4.将验证码保存至redis
        stringRedisTemplate.opsForValue().set(preKey + phone, code);
        stringRedisTemplate.expire(preKey + phone,
                UserRedisKey.USER_CODE_TTL, TimeUnit.SECONDS);
        // 5.发送验证码（短信功能待完成）
        //TODO 后期如果有机会可以将其改为真实的发送手机验证码
        log.info("发送短信验证码成功，验证码为：{}", code);
        log.info(UserRedisKey.CODE_TIME_MESSAGE);
        // 6.响应结果
        return code;
    }

    /**
     * 用户修改密码
     *
     * @param userEditPasswordDTO 修改密码DTO
     * @return LoginResult
     */
    @Override
    public LoginResult editPassword(UserEditPasswordDTO userEditPasswordDTO) {
        String phone = userEditPasswordDTO.getPhone();
        String code = userEditPasswordDTO.getCode();
        // 1.判断手机号是否合法
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 1.1.如果手机号不合法，返回错误信息
            throw new PasswordEditFailedException(UserErrorConstant.PHONE_NUMBER_ERROR);
        }
        // 2.获取redis中验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(UserRedisKey.USER_EDIT_CODE_KEY + phone);
        if (!code.equals(cacheCode)) {
            // 2.1验证码不匹配，修改密码失败，抛出异常
            throw new PasswordEditFailedException(UserErrorConstant.CODE_ERROR);
        }
        // 3.手机号和验证码比对成功，可以修改密码
        userMapper.editPassword(DigestUtil.md5Hex(userEditPasswordDTO.getNewPassword().getBytes()), phone);
        return loginSuccessful(userMapper.selectByPhone(phone));
    }

    /**
     * 用户登出
     *
     * @param userLayoutDTO 登出DTO
     */
    @Override
    public void layout(UserLayoutDTO userLayoutDTO) {
        // 删除redis中的用户登录信息
        stringRedisTemplate.delete(UserRedisKey.USER_KEY + userLayoutDTO.getPhone());
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
     * 展示用户收藏学校
     *
     * @return List<BriefSchoolInfoVO>
     */
    @Override
    public List<BriefSchoolInfoVO> showFavoriteSchool() {
        Long userId = ThreadLocalUtil.getCurrentId();
        List<School> schoolList = userMapper.getUserFavoriteSchool(userId);
        List<BriefSchoolInfoVO> result = new ArrayList<>();
        for (School school : schoolList) {
            BriefSchoolInfoVO briefSchoolInfoVO = new BriefSchoolInfoVO();
            BeanUtil.copyProperties(school, briefSchoolInfoVO);
            result.add(briefSchoolInfoVO);
        }
        return result;
    }

    /**
     * 展示用户收藏专业
     *
     * @return List<BriefMajorVO>
     */
    @Override
    public PageResult<BriefMajorVO> showFavoriteMajor(PageQueryDTO pageQueryDTO) {
        Long userId = ThreadLocalUtil.getCurrentId();
        try (Page<Major> page = PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize())) {
            userMapper.getUserFavoriteMajor(userId);
            List<Major> majorList = page.getResult();
            List<BriefMajorVO> result = new ArrayList<>();
            for (Major major : majorList) {
                BriefMajorVO briefMajorVO = new BriefMajorVO();
                BeanUtil.copyProperties(major, briefMajorVO);
                result.add(briefMajorVO);
            }
            return new PageResult<>(page.getTotal(), result);
        } catch (Exception e) {
            log.error("收藏专业分页查询失败，报错为：{}", e.getMessage());
            return null;
        }
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
     * @param userDeleteDTO 用户注销DTO
     */
    @Override
    public void deleteUser(UserDeleteDTO userDeleteDTO) {
        // 1.获取redis中缓存的验证码
        // 1.1获取用户手机号和验证码
        String phone = userDeleteDTO.getPhone();
        String code = userDeleteDTO.getCode();
        // 1.2获取当前用户id
        long id = ThreadLocalUtil.getCurrentId();
        String cacheCode = stringRedisTemplate.opsForValue().get(UserRedisKey.USER_DELETE_CODE_KEY + phone);
        // 2.进行验证码比对
        if (!code.equals(cacheCode)) {
            // 2.1验证码比对失败，注销失败
            throw new CodeErrorException(UserErrorConstant.CODE_ERROR);
        }
        // 3.验证码比对成功，注销用户
        userMapper.deleteById(id);
        // 4.在redis中删除用户的登录信息
        stringRedisTemplate.delete(UserRedisKey.USER_KEY + phone);
    }

    /**
     * 查看用户评论
     *
     * @return List<String> 用户评论列表
     */
    @Override
    public List<String> showComment(PageQueryDTO pageQueryDTO) {
        return userMapper.selectComment(ThreadLocalUtil.getCurrentId());
    }

}
