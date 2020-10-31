package modules;

import Dto.QuestionResultDto;
import Utils.ConstantsHelper;
import org.apache.commons.codec.Charsets;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class QuestionFromBaidu implements Question {
    private static String _baiduUrlPrefix = "https://www.baidu.com/s?ie=utf-8";
    private List<String> hotWordList;
    private Boolean isSearchFromZhihuOnly;


    public QuestionFromBaidu(List<String> hotWords, Boolean isSearchFromZhihuOnly) {
        hotWordList = hotWords;
        this.isSearchFromZhihuOnly = isSearchFromZhihuOnly;
    }


    public List<String> getHotWordList() {
        return hotWordList;
    }

    public Boolean getSearchFromZhihuOnly() {
        return isSearchFromZhihuOnly;
    }


    @Override
    public List<QuestionResultDto> getQuestion() {
        List<QuestionResultDto> result = new ArrayList<>();
        for (String q : hotWordList
        ) {
            result.addAll(sendHttpGetRequest(q));
        }
        return result;
    }

    private List<QuestionResultDto> sendHttpGetRequest(String keyword) {
        List<QuestionResultDto> results = new ArrayList<>();
        try {
            for (int i = ConstantsHelper.PageHelper.STARTINDEX; i <= ConstantsHelper.PageHelper.MAXPAGENUM; i++) {
                QuestionResultDto pagedResult = new QuestionResultDto();
                int pn = i * ConstantsHelper.PageHelper.PAGESIZE;
                keyword = isSearchFromZhihuOnly ? keyword + " " + "site:www.zhihu.com" : keyword;
                String keyEncode = URLEncoder.encode(keyword, String.valueOf(Charsets.UTF_8));
                StringBuilder sb = new StringBuilder(_baiduUrlPrefix);
                sb.append("&wd=");
                sb.append(keyEncode);
                sb.append("&pn=");
                sb.append(pn);
                sb.append("&bs=");
                sb.append(keyEncode);

                HttpsURLConnection conn = SendRequest.CreateHttpConnection(sb.toString());
                if (conn != null) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sbResp = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sbResp.append(line);
                    }
                    rd.close();
                    conn.disconnect();

                    pagedResult.setPageIndex(i + 1);
                    pagedResult.setPagedHtmlResponse(sbResp.toString());
                    results.add(pagedResult);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private List<QuestionResultDto> parseHtml(List<QuestionResultDto> html)
    {
        List<QuestionResultDto> result = new ArrayList<>();

        return result;
    }
}
