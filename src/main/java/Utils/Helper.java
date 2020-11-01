package Utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.util.Properties;

public class Helper {
    public static Properties GetAppProperties(){
        Properties pro = null;
        try {
            pro = new Properties();
            FileInputStream in = new FileInputStream("./src/main/resources/app.properties");
            pro.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
    }

    public static String getHttpsURLConnectionResponse (HttpsURLConnection connection){
        StringBuilder sbResp = new StringBuilder();

        if(connection != null){
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sbResp.append(line);
                }
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
        }

        return sbResp.toString();
    }
}
