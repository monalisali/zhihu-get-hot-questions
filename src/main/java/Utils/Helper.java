package Utils;

import sun.misc.BASE64Decoder;

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

    public static boolean Base64ToImage(String imgStr,String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片

        if (imgStr.isEmpty()) // 图像数据为空
            return false;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
