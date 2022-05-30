package com.lt.reggie.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;

/**
 * @description: 短信发送工具类
 * @author: 狂小腾
 * @date: 2022/5/29 14:04
 */
public class SMSUtil {

    @Value("${sms.accessKeyId}")
    private static String accessKeyId;

    @Value("${sms.secret}")
    private static String secret;

    @Value("${sms.signName}")
    private static String signName;

    @Value("${sms.templateCode}")
    private static String templateCode;

    /**
     * 发送短信
     *
     * @param phoneNumbers 手机号
     * @param param        参数
     */
    public static void sendMessage(String phoneNumbers, String param) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSysRegionId("cn-hangzhou");
        request.setPhoneNumbers(phoneNumbers);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + param + "\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println("短信发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
