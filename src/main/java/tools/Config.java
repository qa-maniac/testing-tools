package tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private Properties properties = new Properties();

    public Config(String filePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }
}