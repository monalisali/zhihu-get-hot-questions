package modules;

import Dto.QuestionResultDto;

import java.util.ArrayList;
import java.util.List;


public class Crawler {
    public static void main(String[] args) {
        System.out.println("**********************************开始***************************************");

        List<String> hotWords = FileHelper.ReadHotWords();
        QuestionFromBaidu baidu = new QuestionFromBaidu(hotWords, true);
        List<QuestionResultDto> allQuestion = baidu.getQuestion();
        StringBuilder printStringBuilder = new StringBuilder();
        allQuestion.forEach(x->printStringBuilder.append(x.getLink() + "\r\n"));
        System.out.println("一共有" + hotWords.size() + "个热词:");
        System.out.println(hotWords.toString());
        System.out.println("通过热词获取的Zhihu链接，共有" + allQuestion.size() + "个，详细链接：");
        System.out.println(printStringBuilder.toString());

        System.out.println("**********************************结束***************************************");
    }
}
