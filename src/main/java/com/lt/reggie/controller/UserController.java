package com.lt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lt.reggie.common.Result;
import com.lt.reggie.entity.User;
import com.lt.reggie.service.UserService;
import com.lt.reggie.utils.MatilUtil;
import com.lt.reggie.utils.SMSUtil;
import com.lt.reggie.utils.ValidateCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 用户短信管理
 * @author: 狂小腾
 * @date: 2022/5/29 13:43
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MatilUtil matilUtil;

    /**
     * 发送手机短信验证码
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession httpSession) {
        // 获取手机号
//        String phone = user.getPhone();
//
//        if (StringUtils.isNotEmpty(phone)) {
//            // 随机生成4位数验证码
//            String code = ValidateCodeUtil.generateValidateCode(4).toString();
//            log.info("验证码：{}", code);
//
//            // 调用阿里云提供的短信服务API完成发送短信
//            SMSUtil.sendMessage(phone, code);
//
//            // 需要将生成的验证码保存到Session
//            httpSession.setAttribute(phone, code);
//            return Result.success("手机验证码短信发送成功");
//        }
//        return Result.error("手机短信发送失败");

        // 邮箱登录---获取邮箱
        String phone = user.getPhone();
        String subject = "外卖专家登录验证码";
        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtil.generateValidateCode(4).toString();
            String context = "欢迎使用外卖专家，登录验证码为: " + code + ",五分钟内有效，请妥善保管!";
            log.info("code={}", code);

            // 真正地发送邮箱验证码
            matilUtil.sendMail(phone, subject, context);

            // 将随机生成的验证码保存到session中
            httpSession.setAttribute(phone, code);
            return Result.success("验证码发送成功，请及时查看!");
        }
        return Result.error("验证码发送失败，请重新输入!");
    }

    /**
     * 移动端用户登录
     *
     * @param map 也可用DTO代替
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        log.info("移动端用户登录:{}", map.toString());
        // 获取手机号
        String phone = map.get("phone").toString();
        // 获取验证码
        String code = map.get("code").toString();
        // 从session中获取验证码
        Object codeSession = session.getAttribute(phone);
        // 进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeSession != null && codeSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                // 是新用户 自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                user.setName(UUID.randomUUID().toString().substring(0, 5));
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return Result.success(user);
        }
        return Result.error("登录失败");
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        log.info("用户退出");
        request.getSession().removeAttribute("user");
        return Result.success("用户退出成功");
    }
}
