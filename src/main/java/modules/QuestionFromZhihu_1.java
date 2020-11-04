package modules;

import Dto.JingDongRequestDataDto;
import Dto.ZhihuLoginDto;
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
import javax.net.ssl.HttpsURLConnection;
import javax.script.*;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class QuestionFromZhihu_1 {
    private Properties properties = Helper.GetAppProperties();
    private ZhihuLoginDto zhihuLoginDto = new ZhihuLoginDto();

    public void Login() {
        getUnionGoods();
        String captCha = getCaptchaByApi();
        String signature = getHAMCSignature();
        zhihuLoginDto.setCaptcha(captCha);
        zhihuLoginDto.setSignature(signature);
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
        String singStr = zhihuLoginDto.getGrantType() + zhihuLoginDto.getClientId() + zhihuLoginDto.getSource() + zhihuLoginDto.getTimestamp();
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

    private String getUnionGoods(){
        String requestUrl = "https://union.jd.com/api/goods/search";
        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json, text/plain, */*");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
            //conn.setRequestProperty("referer","https://union.jd.com/proManager/index?pageNo=1");
            conn.connect();

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            JSONObject param = new JSONObject();
            JingDongRequestDataDto d = new JingDongRequestDataDto("kt1","st1","电脑");
            param.put("data", d);
            param.put("pageNo", 1);
            param.put("pageSize",60);
            param.put("searchUUID","");
            out.write(param.toString().getBytes("UTF-8"));
            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            System.out.println(sb);



            reader.close();
            // 断开连接
            conn.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /*
    private String getXsrf(){
        String xsrf = "";
        HttpsURLConnection xsrfGetCnn = SendRequest.createHttpConnection("https://www.zhihu.com/",
                Boolean.parseBoolean(properties.getProperty("isConnectedByProxy")),"GET");
        xsrfGetCnn.setInstanceFollowRedirects(false);
        if (xsrfGetCnn != null) {
            String sbResp = Helper.getHttpsURLConnectionResponse(xsrfGetCnn);
            String aa = "aa";
        }

        return xsrf;
    }

    private void encrypt(ZhihuLoginDto dto){
        try {

            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            String jsFileName = "./encrypt.js";   // 读取js文件
            FileReader reader = new FileReader(jsFileName);;  // 执行指定脚本

            engine.eval(reader);
            // 调用merge方法，并传入两个参数
            if(engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;
                String reqeustParam = formatLoginDtoToUrl(dto);
                String s = (String)invoke.invokeFunction("b", URLEncoder.encode(reqeustParam,String.valueOf(Charsets.UTF_8)));
                String aa = "ss";
            }
        } catch (ScriptException | NoSuchMethodException | IOException e) {
            e.printStackTrace();
        }

    }

    private String formatLoginDtoToUrl(ZhihuLoginDto dto){
        StringBuilder sb = new StringBuilder();
        sb.append("client_id=");
        sb.append(dto.getClientId());
        sb.append("&grant_type=");
        sb.append(dto.getGrantType());
        sb.append("&source=");
        sb.append(dto.getSource());
        sb.append("&username=");
        sb.append(dto.getUsername());
        sb.append("&password=");
        sb.append(dto.getPassword());
        sb.append("&lang=");
        sb.append(dto.getLang());
        sb.append("&ref_source=");
        sb.append(dto.getRefSource());
        sb.append("&utm_source=");
        sb.append(dto.getUtmSource());
        sb.append("&captcha=");
        sb.append(dto.getCaptcha());
        sb.append("&timestamp=");
        sb.append(dto.getTimestamp());
        sb.append("&signature=");
        sb.append(dto.getSignature());

    return sb.toString();
    }
    */

}
