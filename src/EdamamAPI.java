import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class EdamamAPI {
    // Статичні налаштування API (ключі доступу зберігаються в окремому класі ConfigAPI)
    private static final ConfigAPI config = new ConfigAPI();
    private static final String BASE_URL = "https://api.edamam.com/api/nutrition-data";

    // --- Основний метод: отримання харчової інформації ---
    public static NutritionalInfo getNutritionalInfo(String foodItem, double grams) throws Exception {
        // Кодуємо назву продукту для використання в URL
        String encodedFoodItem = URLEncoder.encode(foodItem, "UTF-8");

        // Формуємо запит до API
        String urlString = BASE_URL + "?ingr=" + encodedFoodItem +
                "&app_id=" + config.getAppId() + "&app_key=" + config.getAppKey() +
                "&nutrition-type=logging";

        // Ініціалізація HTTP-з'єднання
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // Використовуємо метод GET для отримання даних

        // Перевірка статусу відповіді
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API call failed with response code: " + conn.getResponseCode());
        }

        // Читання відповіді API
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine); // Збираємо відповідь у рядок
        }
        in.close(); // Закриваємо потік

        // Передаємо отриману JSON-відповідь для парсингу
        return parseNutritionalInfo(response.toString(), grams);
    }

    // --- Метод для парсингу JSON-відповіді ---
    private static NutritionalInfo parseNutritionalInfo(String jsonResponse, double grams) {
        // Витягуємо окремі значення для жирів, білків і вуглеводів
        String fatValue = extractValue(jsonResponse, "FAT"); // Жири
        String proteinValue = extractValue(jsonResponse, "PROCNT"); // Білки
        String carbohydratesValue = extractValue(jsonResponse, "CHOCDF"); // Вуглеводи

        // Обчислюємо кількість нутрієнтів на задану вагу продукту
        double fat = fatValue != null ? Double.parseDouble(fatValue) * grams / 100 : 0;
        double protein = proteinValue != null ? Double.parseDouble(proteinValue) * grams / 100 : 0;
        double carbohydrates = carbohydratesValue != null ? Double.parseDouble(carbohydratesValue) * grams / 100 : 0;

        // Повертаємо результати у вигляді об'єкта NutritionalInfo
        return new NutritionalInfo(fat, protein, carbohydrates);
    }

    // --- Допоміжний метод для витягання значення нутрієнтів з JSON-відповіді ---
    private static String extractValue(String jsonResponse, String nutrientKey) {
        // Знаходимо ключ нутрієнта в JSON
        String key = "\"" + nutrientKey + "\":{";
        int keyIndex = jsonResponse.indexOf(key); // Пошук ключа
        if (keyIndex == -1) return null; // Якщо ключ не знайдено, повертаємо null

        // Знаходимо початок і кінець значення нутрієнта
        int startIndex = jsonResponse.indexOf("\"quantity\":", keyIndex) + "\"quantity\":".length();
        int endIndex = jsonResponse.indexOf(",", startIndex); // Значення обмежене комою

        // Повертаємо витягнуте значення як текстовий рядок
        return jsonResponse.substring(startIndex, endIndex).trim();
    }
}