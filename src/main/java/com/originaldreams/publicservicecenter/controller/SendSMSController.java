package com.originaldreams.publicservicecenter.controller;

import com.originaldreams.publicservicecenter.utils.SendEmailUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sendSMS")
public class SendSMSController {
    @RequestMapping(value = "/sendText",method = RequestMethod.GET)
    public ResponseEntity send(String phone,String content){




        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("base");
    }
}
