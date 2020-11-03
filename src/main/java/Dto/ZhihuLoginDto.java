package Dto;

import Utils.Helper;

import java.util.Properties;

public class ZhihuLoginDto {
    private Properties properties = Helper.GetAppProperties();
    private String signature;
    private String captcha;

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
}
