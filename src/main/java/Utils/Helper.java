package Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
}
