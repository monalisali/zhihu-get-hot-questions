package modules;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

public class SendRequest {
    public static CloseableHttpResponse sendHttpGet(String url, boolean isToEncode){
        CloseableHttpResponse httpResponse = null;
        try {
            url = isToEncode ? URLEncoder.encode(url,"UTF-8") : url;
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }


    public static HttpsURLConnection CreateHttpConnection(String requestUrl){
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }
}
