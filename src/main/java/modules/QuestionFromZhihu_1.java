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
            conn.setRequestProperty("Origin","https://union.jd.com");
            conn.setRequestProperty("Accept","application/json, text/plain, */*");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
            conn.setRequestProperty("Cookie", "__jdu=386261381; areaId=2; ipLoc-djd=2-78-51978-0; unpl=V2_ZzNtbUsFQxcmWhJdKRoJUGIEQlgRVUARIA5HACgaVANlVBNVclRCFnQURlRnGFkUZAMZXkNcQBxFCEdkeB5fA2AFEFlBZxBFLV0CFi9JH1c%2bbRVVS1VKEXULTlB8KWwGZzMSXHJVShJ2D0ZWchlsNWAzIm1CX0YQcA52VUsYbEczXxRVQVZEFjgKT1N4HlwHbgMiXHJU; __jdv=76161171|baidu-search|t_262767352_baidusearch|cpc|69838512956_0_8c13bce8c2dd46a4b325d70eb2973f09|1604496028968; shshshfpa=ffc13228-0aca-c07d-fbc1-5458d531853c-1604496031; shshshfp=ddf2c2623a0be24ef08dfdb8b2b93b7e; shshshfpb=wNvQ2qk%20BjJodhketiLDoOA%3D%3D; 3AB9D23F7A4B3C9B=MUAIMUXFJPYAJAC7JJEKOB6YD6ZEQGWKSO3PSPV777KXXNT6ZXY6VKIVINQJCGGVBDE2CPKHDQUX2S2UNSS7JBQ5YA; shshshsID=181a15efc9deeb48c6b941d07aa80f38_12_1604496493374; sidebarStatus=1; identity=47c02a20-f6d2-411c-f1af-edb3d5357c3d; ssid=\"X5UiXJJpRZu2GMCNRMV42A==\"; cid=NXJPMDkwNmdXNzE2N21LOTk2OGFJNzc2MW9SNDUyMmJUODUyM2JBMjc4NHJMMTky; thor=672F9D1F7DD935F9D21850F2696973E864EFB25192EF35B5BE462FF063B3511E95746E641E7FC99BBCF34F4589B8CC1DB66650B29C89FD1D749216B8DDF869C416F4D6D5003C40054EAA63C61420F35B3F212F3D5E19ABF28CBD87A565DBABF628F25CA954B6ACB1CBBC4D0F0FAF60FDE8AE3874A16E3905BF6405116C12ABA7EF29C2F9BDB79EC8F22510A3EF48D2A1; pinId=9dH6xvb346QJw0HQntXn2w; pin=monalisali360; unick=monalisali360; ceshi3.com=000; _tp=48j%2Fo%2F2en7PGkZCTn0apPw%3D%3D; logining=1; _pst=monalisali360; login=true; __jda=209449046.386261381.1604111950.1604111952.1604496029.2; __jdc=209449046; MMsgIdmonalisali360=615683; MNoticeIdmonalisali360=309; __jdb=209449046.56.386261381|2.1604496029; RT=\"z=1&dm=jd.com&si=7qif9hzgldi&ss=kh3fwt2r&sl=y&tt=18zm&ld=kr7h&nu=81457e7b6eec45a5e13d91d267f0378c&cl=k866\"");
            //conn.setRequestProperty("referer","https://union.jd.com/proManager/index?pageNo=1");
            conn.connect();

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            JSONObject param = new JSONObject();
            JingDongRequestDataDto d = new JingDongRequestDataDto("kt1","st1","",9987);
            param.put("data", d);
            param.put("pageNo", 1);
            param.put("pageSize",60);
            param.put("searchUUID","3b05b51cb82046ba8540d73fbf07d91d");
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
