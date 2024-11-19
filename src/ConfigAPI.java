import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigAPI {
    // Шлях до файлу конфігурації
    private static final String CONFIG_FILE = "E:\\Документы\\Проекты IntelliJ\\TEST_API\\src\\configAPI.properties";

    // Поля для збереження ID та ключа API
    private String appId;  // Зберігає значення API ID
    private String appKey; // Зберігає значення API Key

    // Конструктор класу
    public ConfigAPI() {
        loadConfig(); // Автоматично завантажує конфігурацію при створенні об'єкта
    }

    // --- Приватний метод для завантаження конфігурації ---
    private void loadConfig() {
        Properties properties = new Properties(); // Об'єкт для зберігання властивостей з файлу
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            // Завантаження даних з файлу конфігурації
            properties.load(input);
            appId = properties.getProperty("API_id"); // Зчитуємо значення ключа API_id
            appKey = properties.getProperty("API_key"); // Зчитуємо значення ключа API_key
        } catch (IOException e) {
            // Обробка виключень, якщо файл недоступний або має помилки
            e.printStackTrace(); // Виведення деталей помилки
            throw new RuntimeException("Could not load configuration file."); // Завершує програму з повідомленням
        }
    }

    // --- Геттери для доступу до значень API ---
    public String getAppId() {
        return appId; // Повертає API ID
    }

    public String getAppKey() {
        return appKey; // Повертає API Key
    }
}