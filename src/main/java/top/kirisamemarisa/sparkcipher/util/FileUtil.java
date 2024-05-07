package top.kirisamemarisa.sparkcipher.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author Marisa
 * @Description FileUtil.描述
 * @Date 2024/5/7
 */
public class FileUtil {

    public static String readFileContent(String path) {
        File file = new File(path);
        try (FileInputStream fis = new FileInputStream(file)) {
            int size = fis.available();
            byte[] bytes = new byte[size];
            int read = fis.read(bytes);
            if (read > 0) return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
