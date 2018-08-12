package com.originaldreams.publicservicecenter.controller;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.response.MyServiceResponse;
import com.originaldreams.publicservicecenter.utils.SMSUtils;
import com.originaldreams.publicservicecenter.utils.SendEmailUtils;
import com.originaldreams.publicservicecenter.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/SMS")
public class SMSController {
    private Logger logger = LoggerFactory.getLogger(SMSController.class);

    @RequestMapping(value = "/send",method = RequestMethod.GET)
    public ResponseEntity send(String phone,String content) throws Exception {
        MyServiceResponse response =new MyServiceResponse();
        try{
            if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(content)){
                if(SMSUtils.sendSMS(phone,content)){
                    logger.info("发送验证码:"+content+"\t至号码:"+phone);
                    response.setData(content);
                }else{
                    logger.info("发送验证码失败:");
                    return MyResponse.serverError();
                }
            }else{
                return MyResponse.badRequest();
            }
        }catch(Exception e){
            e.printStackTrace();
            return MyResponse.serverError();
        }
        return MyResponse.ok(response);
    }
}
