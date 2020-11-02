package modules;

import Dto.QuestionResultDto;
import Utils.Helper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class Crawler {
    public static void main(String[] args) {
        //测试
        System.out.println("测试开始");
        Properties properties = Helper.GetAppProperties();
        CloseableHttpResponse httpResponse = SendRequest.sendHttpGet(properties.getProperty("zhiCaptchaApiEn"),false);
        HttpEntity entity = httpResponse.getEntity();
        try {
            InputStream is = entity.getContent();
            String html = IOUtils.toString(is, "UTF-8");
            Document document = Jsoup.parse(html);
            JSONObject bodyHtml  = JSON.parseObject(document.getElementsByTag("body").html());
            boolean show_captcha =  bodyHtml.getBooleanValue("show_captcha");
            String capsionTicket = "";
            String img64 = "";
            String capsionCode = "";
            //True: 需要发送第二次
            if(show_captcha){
              Header[] firstRequestRespSetCookie = httpResponse.getHeaders("set-cookie");
                for (Header h: firstRequestRespSetCookie
                     ) {
                    String val = h.getValue();
                    if(val.contains("capsion_ticket")){
                        capsionTicket = val.substring(0,val.indexOf("Domain") - 1);
                        break;
                    }
                }

                //第二次发送时：必须把第一次发送的返回结果：capsion_ticket的值存入到cookie中
                HttpPut httpPut = new HttpPut(properties.getProperty("zhiCaptchaApiEn"));
                httpPut.setHeader("cookie",capsionTicket);
                CloseableHttpClient httpPutClient = HttpClients.createDefault();
                CloseableHttpResponse putResponse = httpPutClient.execute(httpPut);
                HttpEntity putEntity = putResponse.getEntity();

                InputStream httpPutStream = putEntity.getContent();
                String putHtml = IOUtils.toString(httpPutStream, "UTF-8");
                Document putDocument = Jsoup.parse(putHtml);
                JSONObject img64Body  = JSON.parseObject(putDocument.getElementsByTag("body").html());
                img64 = img64Body.getString("img_base64").replace("\n","");
                Helper.Base64ToImage(img64,"C:/temp/zhiHuYanZheng.jpg");
            }

            System.out.println(document);
            System.out.println(img64);
            System.out.println(capsionCode);

        } catch (IOException e) {
            e.printStackTrace();
        }





//        List<String> hotWords = new ArrayList<>();
//        hotWords.add("保温饭盒");
//        QuestionFromZhihu zhihu = new QuestionFromZhihu(hotWords);
//        zhihu.getQuestion();

        //不用翻墙也可以访问：https://readhub.cn/topic/5bMmlAm75lD"
        //翻墙才可以访问： https://www.google.com
//        try {
//            Properties p = Helper.GetAppProperties();
//            if(p != null){
//              boolean isCnnByProxy = Boolean.parseBoolean(p.getProperty("isConnectedByProxy"));
//              HttpsURLConnection resp = SendRequest.createHttpConnection("https://readhub.cn/topic/5bMmlAm75lD", isCnnByProxy);
//              System.out.println("是否使用代理: " + isCnnByProxy);
//              System.out.println("请求返回代码： " + resp.getResponseCode());
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("测试结束");

        //测试

//        System.out.println("**********************************开始***************************************");
//        System.out.println("为了避免被百度屏蔽IP，需要使用代理，请确认做了已下操作：");
//        System.out.println("1. 翻墙软件设置为了「全局模式」");
//        System.out.println("2. 程序的app.properties文件中 isConnectedByProxy = true");
//        System.out.println("3. createHttpConnection() 中的代理协议，IP，端口号与翻墙软件一致。而且代码中，必须使用HTTP模式进行设置");
//        System.out.println("\r\n");
//
//        List<String> hotWords = FileHelper.ReadHotWords();
////        List<String> hotWords = new ArrayList<>();
////        hotWords.add("保温饭盒");
//        QuestionFromBaidu baidu = new QuestionFromBaidu(hotWords, true);
//        List<QuestionResultDto> allQuestion = baidu.getQuestion();
//        StringBuilder printStringBuilder = new StringBuilder();
//        allQuestion.forEach(x -> printStringBuilder.append(x.getLink() + "\r\n"));
//
//        System.out.println("一共有" + hotWords.size() + "个热词:");
//        System.out.println(hotWords.toString());
//        System.out.println("通过热词获取的知乎问题链接，共有" + allQuestion.size() + "个：");
//        System.out.println(printStringBuilder.toString());
//
//        System.out.println("**********************************结束***************************************");
    }
}
