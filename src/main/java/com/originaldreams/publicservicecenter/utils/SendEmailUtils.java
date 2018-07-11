package com.originaldreams.publicservicecenter.utils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailUtils {
    private String host = "smtp.qq.com";
    private String user = "guyexing@foxmail.com";
    private String password = "pmjpoliwxjyadifd";
    public SendEmailUtils(){
        this.session = initSession();
    }
    private  Session session ;
    private  Session initSession(){
        Session session = null;
        try{
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.qq.com");
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");
            //使用JavaMail发送邮件的5个步骤
            //1、创建session
            session = Session.getInstance(prop);
            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return session;
    }
    public void sendSimpleMail(String toEmailAddress,String title,String content)    throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(user));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
        //邮件的标题
        message.setSubject(title);
        //邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        Transport transport = session.getTransport();
        //使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器,用户名和密码都通过验证之后才能够正常发送邮件给收件人
        transport.connect(host,user,password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
