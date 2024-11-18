import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class EdamamAPI {
    private static final ConfigAPI config = new ConfigAPI();
    private static final String BASE_URL = "https://api.edamam.com/api/nutrition-data";

    // Получение информации о питательных веществах
    public static NutritionalInfo getNutritionalInfo(String foodItem, double grams) throws Exception {
        String encodedFoodItem = URLEncoder.encode(foodItem, "UTF-8");
        String urlString = BASE_URL + "?ingr=" + encodedFoodItem +
                "&app_id=" + config.getAppId() + "&app_key=" + config.getAppKey() +
                "&nutrition-type=logging";

        //System.out.println("Request URL: " + urlString); // Тест URL
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API call failed with response code: " + conn.getResponseCode());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return parseNutritionalInfo(response.toString(), grams);
    }

    // Парсинг ответа JSON
    private static NutritionalInfo parseNutritionalInfo(String jsonResponse, double grams) {
        //System.out.println("JSON Response: " + jsonResponse); // Вивід для перевірки структури

        // Використовуємо просту обробку рядка для витягання необхідних даних
        String fatValue = extractValue(jsonResponse, "FAT");
        String proteinValue = extractValue(jsonResponse, "PROCNT");
        String carbohydratesValue = extractValue(jsonResponse, "CHOCDF");

        // Перетворюємо рядки в числові значення
        double fat = fatValue != null ? Double.parseDouble(fatValue) * grams / 100 : 0;
        double protein = proteinValue != null ? Double.parseDouble(proteinValue) * grams / 100 : 0;
        double carbohydrates = carbohydratesValue != null ? Double.parseDouble(carbohydratesValue) * grams / 100 : 0;

        // Повертаємо результати в об'єкті NutritionalInfo
        return new NutritionalInfo(fat, protein, carbohydrates);
    }

    private static String extractValue(String jsonResponse, String nutrientKey) {
        // Шукаємо ключ в jsonResponse
        String key = "\"" + nutrientKey + "\":{";
        int keyIndex = jsonResponse.indexOf(key);
        if (keyIndex == -1) return null;

        int startIndex = jsonResponse.indexOf("\"quantity\":", keyIndex) + "\"quantity\":".length();
        int endIndex = jsonResponse.indexOf(",", startIndex); // Знаходимо кінець значення

        return jsonResponse.substring(startIndex, endIndex).trim(); // Витягуємо і повертаємо значення
    }
}