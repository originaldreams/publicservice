package com.originaldreams.publicservicecenter.controller;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.response.MyServiceResponse;
import com.originaldreams.publicservicecenter.utils.SendSMSUtils;
import com.originaldreams.publicservicecenter.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/SMS")
public class SMSController {
    private Logger logger = LoggerFactory.getLogger(SMSController.class);

    @RequestMapping(value = "/sendVerificationCode",method = RequestMethod.GET)
    public ResponseEntity sendVerificationCode(String phone){
        MyServiceResponse response =new MyServiceResponse();
        if(phone == null || phone.isEmpty()){
            return MyResponse.badRequest();
        }else{
            if(SendSMSUtils.sendVerificationCode(phone)){
                return MyResponse.ok(response);
            }else {
                response.setSuccess(MyServiceResponse.SUCCESS_CODE_FAILED);
                response.setMessage("验证码发送失败");
                return MyResponse.ok(response);
            }
        }
    }
}
