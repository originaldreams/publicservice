package com.originaldreams.publicservicecenter.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.originaldreams.publicservicecenter.controller.SMSController;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 短信发送工具类
 * Created by yangkaile on 2017/4/20.
 */
public class SMSUtils {
    private static       Logger logger        = LoggerFactory.getLogger(SMSController.class);
    private static final String SERVER_IP     = "sandboxapp.cloopen.com";
    private static final String SERVER_PORT   = "8883";
    private static final String ACCOUNT_SID   = "8aaf07085b5fee9a015b85e01170103c";
    private static final String ACCOUNT_TOKEN = "07b5328498f943769fd1f631ef737fba";
    public static final  String App_ID        = "8aaf07085b5fee9a015b85e011ea1042";


    public static boolean sendSMS(String phone, String verificationCode) throws Exception {
        String result = sendTemplateSMS(phone, "1", new String[]{ verificationCode, "10" });
        System.out.println("SDKTestGetSubAccounts result=" + result);
        JsonObject result_json = new JsonParser().parse(result).getAsJsonObject();
        String     statusCode  = result_json.getAsJsonPrimitive("statusCode").getAsString();
        System.out.println(statusCode);
        if ("000000".equals(statusCode)) {
            return true;
        } else {
            logger.info(result_json.get("statusCode").toString());
            return false;
        }
    }

    public static String sendTemplateSMS(String to, String templateId, String[] datas) {
        DefaultHttpClient httpclient = null;

        try {
            httpclient = registerSSL(SERVER_IP, "TLS", Integer.parseInt(SERVER_PORT), "https");
        } catch (Exception var24) {
            var24.printStackTrace();
            throw new RuntimeException("初始化httpclient异常" + var24.getMessage());
        }

        String result = "";

        label252:
        {
            HashMap var17;
            try {
                HttpPost   httppost    = (HttpPost) getHttpRequestBase(1, "SMS/TemplateSMS");
                String     requsetbody = "";
                int        var13;
                JsonObject json        = new JsonObject();
                json.addProperty("appId", App_ID);
                json.addProperty("to", to);
                json.addProperty("templateId", templateId);
                if (datas != null) {
                    StringBuilder sb = new StringBuilder("[");
                    for (int index = 0; index < datas.length; ++index) {
                        String s = datas[ index ];
                        sb.append("\"" + s + "\"" + ",");
                    }

                    sb.replace(sb.length() - 1, sb.length(), "]");
                    JsonParser parser = new JsonParser();
                    JsonArray  Jarray = parser.parse(sb.toString()).getAsJsonArray();
                    json.add("datas", Jarray);
                }

                requsetbody = json.toString();

                System.out.println("sendTemplateSMS Request body =  " + requsetbody);
                BasicHttpEntity requestBody = new BasicHttpEntity();
                requestBody.setContent(new ByteArrayInputStream(requsetbody.getBytes("UTF-8")));
                requestBody.setContentLength((long) requsetbody.getBytes("UTF-8").length);
                httppost.setEntity(requestBody);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity   entity   = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                }

                EntityUtils.consume(entity);
                break label252;
            } catch (IOException var25) {
                var25.printStackTrace();
                System.out.println(var25.getMessage());
            } catch (Exception var26) {
                var26.printStackTrace();
                System.out.println(var26.getMessage());
            } finally {
                if (httpclient != null) {
                    httpclient.getConnectionManager().shutdown();
                }

            }

        }

        System.out.println("sendTemplateSMS response body = " + result);
        return result;
    }

    public static String dateToStr(Date date, String pattern) {
        if (date != null && !date.equals("")) {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.format(date);
        } else {
            return null;
        }
    }

    private static HttpRequestBase getHttpRequestBase(int get, String action) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String      timestamp  = dateToStr(new Date(), "yyyyMMddHHmmss");
        EncryptUtil eu         = new EncryptUtil();//TODO
        String      sig        = "";
        String      acountName = "";
        String      acountType = "Accounts";
        acountName = ACCOUNT_SID;
        sig = ACCOUNT_SID + ACCOUNT_TOKEN + timestamp;
        String signature = eu.md5Digest(sig);
        String url       = getBaseUrl().append("/" + acountType + "/").append(acountName).append("/" + action + "?sig=").append(signature).toString();
        System.out.println(getmethodName(action) + " url = " + url);
        HttpRequestBase mHttpRequestBase = null;
        if (get == 0) {
            mHttpRequestBase = new HttpGet(url);
        } else if (get == 1) {
            mHttpRequestBase = new HttpPost(url);
        }

        setHttpHeader((AbstractHttpMessage) mHttpRequestBase);
        String src  = acountName + ":" + timestamp;
        String auth = eu.base64Encoder(src);
        ((HttpRequestBase) mHttpRequestBase).setHeader("Authorization", auth);
        return (HttpRequestBase) mHttpRequestBase;
    }


    public static DefaultHttpClient registerSSL(String hostname, String protocol, int port, String scheme) throws NoSuchAlgorithmException, KeyManagementException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        SSLContext        ctx        = SSLContext.getInstance(protocol);
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                if (chain != null && chain.length != 0) {
                    if (authType != null && authType.length() != 0) {
                        boolean           br        = false;
                        Principal         principal = null;
                        X509Certificate[] var8      = chain;
                        int               var7      = chain.length;

                        for (int var6 = 0; var6 < var7; ++var6) {
                            X509Certificate x509Certificate = var8[ var6 ];
                            principal = x509Certificate.getSubjectX500Principal();
                            if (principal != null) {
                                br = true;
                                return;
                            }
                        }

                        if (!br) {
                            throw new CertificateException("服务端证书验证失败！");
                        }
                    } else {
                        throw new IllegalArgumentException("null or zero-length authentication type");
                    }
                } else {
                    throw new IllegalArgumentException("null or zero-length certificate chain");
                }
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[ 0 ];
            }
        };
        ctx.init((KeyManager[]) null, new TrustManager[]{ tm }, new SecureRandom());
        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme           sch           = new Scheme(scheme, port, socketFactory);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
        return httpclient;
    }


    private static StringBuffer getBaseUrl() {
        StringBuffer sb = new StringBuffer("https://");
        sb.append(SERVER_IP).append(":").append(SERVER_PORT);
        sb.append("/2013-12-26");
        return sb;
    }

    private static String getmethodName(String action) {
        return action.equals("SMS/TemplateSMS") ? "sendTemplateSMS" : "";
    }

    private static void setHttpHeader(AbstractHttpMessage httpMessage) {
        httpMessage.setHeader("Accept", "application/json");
        httpMessage.setHeader("Content-Type", "application/json;charset=utf-8");
    }

    public static void main(String[] args) {
//        try {
////            SMSUtils mySMSUtils = new SMSUtils();
//            SMSUtils.sendSMS("15515939171", "12456");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String context="{\"statusCode\":\"000000\",\"templateSMS\":{\"smsMessageSid\":\"16eeeef7c9fb4aa287a6f98163680265\",\"dateCreated\":\"20180812161456\"}}";
        JsonObject result_json = new JsonParser().parse(context).getAsJsonObject();
        String statusCode=result_json.getAsJsonPrimitive("statusCode").getAsString();

        if ("000000".equals(statusCode)) {
            System.out.println("true:"+statusCode);
        } else {
            System.out.println("false:"+statusCode);

        }


    }
}
