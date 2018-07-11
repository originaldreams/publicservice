package com.originaldreams.publicservicecenter.controller;

import com.originaldreams.publicservicecenter.utils.SendEmailUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sendEmail")
public class SendEmailController {
    @RequestMapping(value = "/base",method = RequestMethod.GET)
    public ResponseEntity base(){
        try{
            new SendEmailUtils().sendSimpleMail("2211679802@qq.com","发送一封纯文本邮件","20180711_1616测试邮件！！！");
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("base");
    }
}
