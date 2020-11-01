package modules;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import javax.net.ssl.HttpsURLConnection;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Properties;

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


    public static HttpsURLConnection createHttpConnection(String requestUrl,boolean isConnectedByProxy){
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(requestUrl);
            if(isConnectedByProxy){
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 58591));
                conn = (HttpsURLConnection) url.openConnection(proxy);
            }
            else{
                conn = (HttpsURLConnection) url.openConnection();
            }
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,/;q=0.8,application/signed-exchange;v=b3;q=0.9");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static String getHttpResponseLocation(String requestUrl){
        String location = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(requestUrl).openConnection();
            conn.setInstanceFollowRedirects(false);
            location = conn.getHeaderField("Location");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }
}
