package developer.macbury.betadroid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class Utils {

  public static String loadString(String path) {
    try {
      FileInputStream inputStream = new FileInputStream(path);
      BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder total = new StringBuilder();
      String line;
      while ((line = r.readLine()) != null) {
        total.append(line);
      }
      r.close();
      inputStream.close();
      return total.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void saveString(String path, String json) {
    try {
      FileOutputStream outputStream = new FileOutputStream(path);
      outputStream.write(json.getBytes());
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
