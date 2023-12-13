package cn.cstengxun.deng.dome1.controller;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName EailController
 * @Description TODO
 * @Author Mr.Deng
 * @Date 2023/11/29 15:52
 * @Version 1.0
 */
@RestController
@RequestMapping("/Email")
public class EmailController {


    //TODO:自动装配Spring-Boot的发送邮件的接口
    @Resource
    private JavaMailSender mailSender;


    //Redis模板
    @Resource
    private RedisTemplate redisTemplate;

    //获取发件人的账户
    @Value("${spring.mail.userName}")
    private String sender;

    //获取发件人昵称
    @Value("${spring.mail.nickname}")
    private String nickName;

    /*发送邮件验证码接口*/
    @RequestMapping("/sends")
    public String sendCode(@RequestParam("email") String email) {
        System.out.println(email);
        //获得邮箱对象
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //获得来自发送人的信息
        simpleMailMessage.setFrom(nickName + '<' + sender + '>');
        //获取前端提交过来的邮箱
        simpleMailMessage.setTo(email);
        //
        simpleMailMessage.setSubject("欢迎访问湖南省人民政府");

        //使用tutool工具生成六位随机数
        String code = RandomUtil.randomNumbers(6);

        //TODO: 将随机验证码存到Redis，并设置有效时间，用户输入验证码与Redis中存放的进行比较

        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

        String context = "【验证码】您的验证码为：" + code + " 。 验证码五分钟内有效，逾期作废。\n\n\n" +
                "------------------------------\n\n\n" +
                "湖南省人民政府" +
                "更多可访问:\n\n" +
                "http://www.hunan.gov.cn/\n\n";

        simpleMailMessage.setText(context);
        mailSender.send(simpleMailMessage);

        return "发送成功";
    }

    /**
     * @return java.lang.String
     * @description :
     * @author Mr.Deng
     * @date 2023/12/1 18:07
     * @param        email、 code
     */
    /*验证邮件验证码接口*/
    @PostMapping("/ver")
    public String Verification(@RequestParam String email, @RequestParam String code) {
        //使用Redis进行验证
        //当Redis中查询不到该键：邮箱 时，代表键：验证码代码已过期
        Object obj = redisTemplate.opsForValue().get(email);

        //使用hutool工具包中的object判断工具
        if (ObjUtil.isEmpty(obj)) {

            return "验证码失效";
        }

        //取出redis中的验证码
        String RedisCode = (String) obj;

        //对验证码进行校验
        if (code.equals(RedisCode)) {

            return "验证通过";
        }
        return "验证码输入错误";
    }
}
