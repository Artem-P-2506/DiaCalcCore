import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigAPI {
    private static final String CONFIG_FILE = "E:\\Документы\\Проекты IntelliJ\\TEST_API\\src\\configAPI.properties"; // Ім'я файлу конфігурації
    private String appId;  // Змінна для зберігання API_id
    private String appKey; // Змінна для зберігання API_key

    public ConfigAPI() {
        loadConfig(); // Завантаження конфігурації під час створення об'єкта
    }

    private void loadConfig() {
        Properties properties = new Properties(); // Створення об'єкта Properties для зберігання значень
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) { // Відкриття файлу конфігурації
            properties.load(input); // Завантаження властивостей з файлу
            appId = properties.getProperty("API_id"); // Отримання API_id
            appKey = properties.getProperty("API_key"); // Отримання API_key
        } catch (IOException e) { // Обробка виключення при помилках вводу/виводу
            e.printStackTrace(); // Виведення стеку виключення
            throw new RuntimeException("Could not load configuration file."); // Генерація виключення
        }
    }

    public String getAppId() {
        return appId; // Метод для отримання API_id
    }

    public String getAppKey() {
        return appKey; // Метод для отримання API_key
    }
}