package modules;

import Dto.QuestionResultDto;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    public static void main(String[] args) {
        System.out.println("**********************************开始***************************************");
        System.out.println(FileHelper.ReadHotWords());
        List<String > hotWords = new ArrayList<>();
        hotWords.add("保温饭盒");
        QuestionFromBaidu baidu = new QuestionFromBaidu(hotWords,true);
        List<QuestionResultDto> r = baidu.getQuestion();
        System.out.println("**********************************结束***************************************");
    }
}
