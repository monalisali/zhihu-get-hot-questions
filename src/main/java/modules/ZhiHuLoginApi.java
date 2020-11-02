package modules;

import Utils.Helper;

import java.util.Properties;

public class ZhiHuLoginApi {
    private Properties properties = Helper.GetAppProperties();
    //Api中的参数
    private String zhiHuLoginApi;
    private String clientId;
    private String captcha;
    private String grantType;
    private String lang;
    private String password;
    private String refSource;
    private String signature;
    private String source;
    private String timestamp;
    private String username;
    private String utmSource;


    //Header中的参数
    private String acceptEncoding;
    private String host;
    private String referer;
    private String userAgent;

    public String getUserAgent() {
        userAgent = properties.getProperty("userAgent");
        return userAgent;
    }


    public String getReferer() {
        referer = properties.getProperty("zhiHuRefer");
        return referer;
    }


    public String getHost() {
        host = properties.getProperty("zhiHuHost");
        return host;
    }


    public String getAcceptEncoding() {
        acceptEncoding = properties.getProperty("acceptEncoding");
        return acceptEncoding;
    }

    public String getZhiHuLoginApi() {
        zhiHuLoginApi = properties.getProperty("zhiHuLoginApi");
        return zhiHuLoginApi;
    }

    public String getUtmSource() {
        utmSource = "";
        return utmSource;
    }

    public String getUsername() {
        username = properties.getProperty("userName");
        return username;
    }

    public String getTimestamp() {
        return timestamp = String.valueOf(System.currentTimeMillis());
    }


    public String getSource() {
        source = properties.getProperty("source");
        return source;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRefSource() {
        refSource = properties.getProperty("refSource");
        return refSource;
    }


    public String getPassword() {
        password = properties.getProperty("thePassword");
        return password;
    }

    public String getLang() {
        lang = properties.getProperty("theLang");
        return lang;
    }


    public String getGrantType() {
        grantType = properties.getProperty("grantType");
        return grantType;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }


    public String getClientId() {
        clientId = properties.getProperty("clientId");;
        return clientId;
    }

}
