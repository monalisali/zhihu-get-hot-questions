package modules;


import org.apache.commons.codec.Charsets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
      //文件中只放入需要查询回答的热词，历史记录不要放
      private static String _hotWordsFilePath = "./hotWordsToQuestion.txt";

      public static List<String> ReadHotWords(){
          List<String> hotWords = new ArrayList<>();
          Path path = Paths.get(_hotWordsFilePath);
          try {
             hotWords =  Files.readAllLines(path,Charsets.UTF_8);
          } catch (IOException e) {
              e.printStackTrace();
          }
          return hotWords;
      }

}
