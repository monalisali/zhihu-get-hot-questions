package modules;

import Utils.Helper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.crypto.provider.HmacSHA1;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class ZhiHuLoginApi {
    private Properties properties = Helper.GetAppProperties();
    private String captcha;
    private String signature;

    public String getUserAgent() {
        return properties.getProperty("userAgent");
    }


    public String getReferer() {
        return properties.getProperty("zhiHuRefer");
    }


    public String getHost() {
        return properties.getProperty("zhiHuHost");
    }


    public String getAcceptEncoding() {
        return properties.getProperty("acceptEncoding");
    }

    public String getZhiHuLoginApi() {
        return properties.getProperty("zhiHuLoginApi");
    }

    public String getUtmSource() {
        return "";
    }

    public String getUsername() {
        return properties.getProperty("userName");
    }

    public String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }


    public String getSource() {
        return properties.getProperty("source");
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRefSource() {
        return properties.getProperty("refSource");
    }


    public String getPassword() {
        return properties.getProperty("thePassword");
    }

    public String getLang() {
        return properties.getProperty("theLang");
    }


    public String getGrantType() {
        return properties.getProperty("grantType");
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }


    public String getClientId() {
        return properties.getProperty("clientId");
    }

    public void Login() {
        String captCha = getCaptchaByApi();
        String signature = getHAMCSignature();
        setCaptcha(captCha);
        setSignature(signature);

    }

    private String getCaptchaByApi() {
        String captchaCode = "";
        try {
            String img64 = "";
            String capsionTicket = "";
            CloseableHttpResponse chkCaptResp = SendRequest.sendHttpGet(properties.getProperty("zhiCaptchaApiEn"), false);
            HttpEntity chkCaptEntity = chkCaptResp.getEntity();
            InputStream chkCaptIs = chkCaptEntity.getContent();
            String chkCaptHtml = IOUtils.toString(chkCaptIs, String.valueOf(Charsets.UTF_8));
            Document chkCaptDocument = Jsoup.parse(chkCaptHtml);
            JSONObject bodyHtml = JSON.parseObject(chkCaptDocument.getElementsByTag("body").html());
            boolean show_captcha = bodyHtml.getBooleanValue("show_captcha");
            //show_captcha = true 说明登录时需要验证码，重新用HttpPut再发送一次请求以获取验证图片
            if (show_captcha) {
                Header[] chkCaptRespSetCookie = chkCaptResp.getHeaders("set-cookie");
                for (Header h : chkCaptRespSetCookie
                ) {
                    String val = h.getValue();
                    if (val.contains("capsion_ticket")) {
                        capsionTicket = val.substring(0, val.indexOf("Domain") - 1);
                        break;
                    }
                }

                //第二次发送时：必须把第一次发送的返回结果中"capsion_ticket"的值存入到cookie中
                HttpPut httpPut = new HttpPut(properties.getProperty("zhiCaptchaApiEn"));
                httpPut.setHeader("cookie", capsionTicket);
                CloseableHttpClient httpPutClient = HttpClients.createDefault();
                CloseableHttpResponse putResponse = httpPutClient.execute(httpPut);
                HttpEntity putEntity = putResponse.getEntity();
                InputStream httpPutStream = putEntity.getContent();
                String putHtml = IOUtils.toString(httpPutStream, "UTF-8");
                Document putDocument = Jsoup.parse(putHtml);
                JSONObject img64Body = JSON.parseObject(putDocument.getElementsByTag("body").html());
                img64 = img64Body.getString("img_base64").replace("\n", "");
                Helper.SaveBase64ToImage(img64, "C:/temp/zhiHuYanZheng.jpg");
                //Todo:使用解析验证码的库来解析图片
                //captchaCode = !!!!!!!!!!!!;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Todo: 使用解析验证码的库来解析图片完成前，在这里打个断点，手动为captchaCode赋值
        return captchaCode;
    }

    private String getHAMCSignature() {
        String hex = "";
        String singStr = getGrantType() + getClientId() + getSource() + getTimestamp();
        try {
            String key = properties.getProperty("HAMCShaKey");
            byte[] data = key.getBytes(String.valueOf(Charsets.UTF_8));
            SecretKey secretKey = new SecretKeySpec(data, properties.getProperty("HmacSHA1"));
            // 生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(properties.getProperty("HmacSHA1"));
            mac.init(secretKey);
            byte[] text = singStr.getBytes(String.valueOf(Charsets.UTF_8));
            byte[] encryptByte = mac.doFinal(text);
            hex = Hex.encodeHexString(encryptByte);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return hex;
    }

}
