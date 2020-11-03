package modules;


import Dto.QuestionResultDto;

import java.util.List;

public class Crawler {
    public static void main(String[] args) {
        //测试
        System.out.println("测试开始");

//        QuestionFromZhihu_1 loginApi = new QuestionFromZhihu_1();
//        loginApi.Login();
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
        System.out.println("为了避免被百度屏蔽IP，需要使用代理，请确认做了已下操作：");
        System.out.println("1. 翻墙软件设置为了「全局模式」");
        System.out.println("2. 程序的app.properties文件中 isConnectedByProxy = true");
        System.out.println("3. createHttpConnection() 中的代理协议，IP，端口号与翻墙软件一致。而且代码中，必须使用HTTP模式进行设置");
        System.out.println("\r\n");

        List<String> hotWords = FileHelper.ReadHotWords();
//        List<String> hotWords = new ArrayList<>();
//        hotWords.add("保温饭盒");
        QuestionFromBaidu baidu = new QuestionFromBaidu(hotWords, true);
        List<QuestionResultDto> allQuestion = baidu.getQuestion();
        StringBuilder printStringBuilder = new StringBuilder();
        allQuestion.forEach(x -> printStringBuilder.append(x.getLink() + "\r\n"));

        System.out.println("一共有" + hotWords.size() + "个热词:");
        System.out.println(hotWords.toString());
        System.out.println("通过热词获取的知乎问题链接，共有" + allQuestion.size() + "个：");
        System.out.println(printStringBuilder.toString());

        System.out.println("**********************************结束***************************************");
    }
}
