package modules;

import Dto.QuestionResultDto;
import Utils.ConstantsHelper;
import org.apache.commons.codec.Charsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class QuestionFromBaidu implements Question {
    private static String _baiduUrlPrefix = "https://www.baidu.com/s?ie=utf-8";
    private static String _zhihuSpecificSite = "site:www.zhihu.com";
    //所有热词
    private List<String> hotWordList;
    //搜索时，是否加上site:www.zhihu.com
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
        List<QuestionResultDto> pagedHtmlList = new ArrayList<>();
        List<QuestionResultDto> result = new ArrayList<>();
        for (String q : hotWordList
        ) {
            pagedHtmlList.addAll(sendHttpGetRequest(q));
        }
        List<QuestionResultDto> links = parsePagedHtml(pagedHtmlList);
        result.addAll(links.stream().distinct().collect(Collectors.toList()));
        return result;
    }

    private List<QuestionResultDto> sendHttpGetRequest(String keyword) {
        List<QuestionResultDto> results = new ArrayList<>();
        try {
            for (int i = ConstantsHelper.PageHelper.STARTINDEX; i <= ConstantsHelper.PageHelper.MAXPAGENUM; i++) {
                QuestionResultDto pagedResult = new QuestionResultDto();
                int pn = i * ConstantsHelper.PageHelper.PAGESIZE;
                keyword = isSearchFromZhihuOnly ? keyword + " " + _zhihuSpecificSite : keyword;
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

    private List<QuestionResultDto> parsePagedHtml(List<QuestionResultDto> pagedHmls) {
        List<QuestionResultDto> result = new ArrayList<>();
        for(int i = 0 ; i < pagedHmls.size();i++){
            QuestionResultDto html = pagedHmls.get(i);
            Document document = Jsoup.parse(html.getPagedHtmlResponse());
            Elements elements = document.getElementsByTag("h3");
            for (Element el : elements
            ) {
                Element link = el.select("a").first();
                String href = link.attr("href");
                if (!href.isEmpty()) {
                    QuestionResultDto temp = new QuestionResultDto();
                    temp.setLinkIndex(i + 1);
                    temp.setLink(href);
                    result.add(temp);
                }
            }
        }
        return result;
    }
}
