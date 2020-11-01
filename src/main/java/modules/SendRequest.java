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

    public static HttpsURLConnection createHttpConnectionTest(String requestUrl,boolean isConnectedByProxy){
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
            //conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("accept","*/*");
            conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
            conn.setRequestProperty("accept-encoding","gzip, deflate, br");
            conn.setRequestProperty("pragma","no-cache");
            //conn.setRequestProperty("authority","www.zhihu.com");
            //conn.setRequestProperty("cookie","_zap=b8a0dec9-923f-40c6-bba1-88b01cdbc158; d_c0=\"AGBR-L8zHRKPTvWEKpab1ey-32oaWHt01qs=|1603977368\"; _xsrf=8a0421a3-4808-434b-bf7b-da154fe66c5e; q_c1=313db3424e7e4bf2a944f98567eb539c|1604205062000|1604205062000; tshl=; tst=h; SESSIONID=91k26BIyE29dudW6ZYRZV5QEXPSchlEpui1oIHpgRuO; JOID=UVgXAkx8B0IbyyTmDX5gF6OcH_weRjQqL_llrXc9Nnl8uESyOZCt2UbMJecI5gS-nUQgwbWZ4YFYbM4IxEHonuE=; osd=UVETBkt8DkYfzCTvCXpnF6qYG_seTzAuKPlsqXM6NnB4vEOyMJSp3kbFIeMP5g26mUMgyLGd5oFRaMoPxEjsmuY=; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1604206440,1604234406,1604234706,1604234839; capsion_ticket=\"2|1:0|10:1604235269|14:capsion_ticket|44:ZDYwOTAyMTY5YzhlNDdlZTkyYjNlYzQ0NThkMzA4ZTg=|d6166e299d38947f1ab87f2eb1ad2549c232e5a69334fad5cb600bff7b467c7c\"; z_c0=\"2|1:0|10:1604235295|4:z_c0|92:Mi4xTnJJVUFnQUFBQUFBWUZINHZ6TWRFaVlBQUFCZ0FsVk5IXzZMWUFDSUtCYUNoSU45UUU1Nks2azU4ZjA5RWNva2Zn|1295e86ef2aa726b0ee1dadbc9c9140cf26aff0bb7f8af0051ca57bb54d08e16\"; unlock_ticket=\"ABDMoNyNrggmAAAAYAJVTSe3nl8qOEvhuXFwxnxWdv9dUlA-xdNlkw==\"; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1604235296; KLBRSID=031b5396d5ab406499e2ac6fe1bb1a43|1604235307|1604231323");
            //conn.setRequestProperty("referer","https://www.zhihu.com/search?type=content&q=%E4%BF%9D%E6%B8%A9%E9%A5%AD%E7%9B%92");
//            conn.setRequestProperty("sec-fetch-dest","empty");
//            conn.setRequestProperty("sec-fetch-mode","cors");
//            conn.setRequestProperty("sec-fetch-site","same-origin");
//            conn.setRequestProperty("x-api-version","3.0.91");
//            conn.setRequestProperty("x-app-za","OS=Web");
//            conn.setRequestProperty("x-requested-with","fetch");
//            conn.setRequestProperty("x-zse-83","3_2.0");
//            conn.setRequestProperty("x-zse-86","1.0_aXFqS4Xqc8Ofk02qZU20rHe0NUFY28O8z9tyoT90rTtp");
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
