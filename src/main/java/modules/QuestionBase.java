package modules;

import java.util.List;

public class QuestionBase {
    //所有热词
    private List<String> hotWordList;

    public QuestionBase(List<String> hotWordList){
        this.hotWordList = hotWordList;

    }

    public List<String> getHotWordList() {
        return hotWordList;
    }

    public void setHotWordList(List<String> hotWordList) {
        this.hotWordList = hotWordList;
    }
}

