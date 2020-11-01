package modules;

import Dto.QuestionResultDto;
import Utils.Helper;
import org.apache.commons.codec.Charsets;
import org.apache.http.client.methods.CloseableHttpResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QuestionFromZhihu extends QuestionBase implements IQuestion {
    private Properties properties = Helper.GetAppProperties();
    private boolean isConnectByProxy = Boolean.parseBoolean(properties.getProperty("isConnectedByProxy"));
    public QuestionFromZhihu (List<String> hotWords){
        super(hotWords);
    }

    @Override
    public List<QuestionResultDto> getQuestion() {
        for (String h: this.getHotWordList()
             ) {
           sendHttpGetRequest(h, isConnectByProxy);
        }
        return null;
    }

    private List<QuestionResultDto> sendHttpGetRequest(String keyword, boolean isConnectedByProxy){
        List<QuestionResultDto> results = new ArrayList<>();
        try {
            //https://www.zhihu.com/api/v4/search_v3?t=general&q=%E4%BF%9D%E6%B8%A9%E9%A5%AD%E7%9B%92&correction=1&offset=0&limit=20&lc_idx=0&show_all_topics=0
            String prefix = properties.getProperty("zhiUrlPrefix");
            StringBuilder url = new StringBuilder(prefix);
            url.append("&q=");
            url.append(URLEncoder.encode(keyword, String.valueOf(Charsets.UTF_8)));
            url.append("&correction=1&offset=0&limit=20&lc_idx=0&show_all_topics=0");
            HttpsURLConnection conn = SendRequest.createHttpConnectionTest(url.toString(), isConnectedByProxy);
            if(conn != null){
               String resp = Helper.getHttpsURLConnectionResponse(conn);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return results;
    }
}
