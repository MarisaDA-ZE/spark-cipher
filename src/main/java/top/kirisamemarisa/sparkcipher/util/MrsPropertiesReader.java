package top.kirisamemarisa.sparkcipher.util;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import top.kirisamemarisa.sparkcipher.annotations.StaticValue;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @Author Marisa
 * @Description 使用此工具类可以向静态的类中注入属性值
 * @Date 2024/5/23
 */
public class MrsPropertiesReader {

    /**
     * 装载属性文件
     *
     * @param fileName 文件路径
     * @param clazz    目标类
     * <p>注意：这个方法很可能不支持多线程，多个线程同时调用装载不同的值可能要遭</p>
     */
    public static <T> void loadProperties(String fileName, Class<T> clazz) {
        Resource resource = new ClassPathResource(fileName, ClassLoader.getSystemClassLoader());
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(StaticValue.class)) {
                    StaticValue annotation = field.getAnnotation(StaticValue.class);
                    String key = annotation.value();
                    Object value = properties.get(key);
                    if (value != null) {
                        field.setAccessible(true);
                        field.set(instance, value);
                    } else {
                        System.out.println("值未找到");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("资源不存在！", e);
        }
    }

    public static void main(String[] args) {
        System.out.println("main...");
    }
}
