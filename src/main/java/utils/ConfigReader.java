package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream is =
                     ConfigReader.class.getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties not found");
            }
            properties.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    // 1. Purana method (wahi rakho)
    public static boolean isHealingEnabled() {
        return Boolean.parseBoolean(
                properties.getProperty("healing.enabled", "false"));
    }

    // 2. Purana method (wahi rakho)
    public static String getGeminiKey() {
        return properties.getProperty("gemini.key");
    }

    /**
     * 🔥 Naya Method: Generic key reader
     * Isse hum ai.type, model.name etc. sab nikal sakte hain
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}