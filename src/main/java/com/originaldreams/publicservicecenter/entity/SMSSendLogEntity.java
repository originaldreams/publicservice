package com.originaldreams.publicservicecenter.entity;

public class SMSSendLogEntity {
    private String phone;
    private Integer type;
    private String templateId ;
    private String firstKey;
    private String secondKey;
    //{data={templateSMS={dateCreated=20180827170721, smsMessageSid=a21809d2dbe84872878a3e9cd9a3da17}}, statusCode=000000}
    private String result;
    //000000
    private String statusCode;

}
