package com.originaldreams.publicservicecenter.controller;

import com.originaldreams.publicservicecenter.utils.SendEmailUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sendEmail")
public class SendEmailController {
    @RequestMapping(value = "/sendText",method = RequestMethod.GET)
    public ResponseEntity sendText(String emailAddress,String title,String content){
        try{
            if(emailAddress == null || title == null || content == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("请求参数异常！！！");
            }else{
                new SendEmailUtils().sendSimpleMail(emailAddress,title,content);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).contentType(MediaType.APPLICATION_JSON).body("服务异常！！！" + e.getMessage());

        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("base");
    }
}
