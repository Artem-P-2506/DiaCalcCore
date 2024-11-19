import java.util.InputMismatchException;
import java.util.Scanner;

public class DiaCalcCoreTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- Крок 1: Введення базових даних для налаштування розрахунків ---
        // Користувач вводить загальну добову дозу інсуліну та цільовий рівень цукру
        double totalDailyDose = getDoubleInput(scanner, "Введіть загальну добову дозу інсуліну: ");
        double targetSugarLevel = getDoubleInput(scanner, "Введіть цільовий рівень цукру в крові (ммоль/л): ");

        // Визначення чи є підвищена або знижена потреба в інсуліні
        boolean increasedInsulinRequirement = getBooleanInput(scanner, "Чи є підвищена потреба в інсуліні? (+/-): ");
        boolean decreasedInsulinRequirement = !increasedInsulinRequirement ?
                getBooleanInput(scanner, "Чи є знижена потреба в інсуліні? (+/-): ") : false;

        // Введення коефіцієнтів для розрахунку інсуліну в залежності від часу прийому їжі (якщо використовувати Не стандартний калькулятор)
        double breakfastRatio = getDoubleInput(scanner, "Скільки інсуліну потрібно на 1 ХЕ на сніданок: ");
        double lunchRatio = getDoubleInput(scanner, "Скільки інсуліну потрібно на 1 ХЕ на обід: ");
        double dinnerRatio = getDoubleInput(scanner, "Скільки інсуліну потрібно на 1 ХЕ на вечерю: ");

        // Чи потрібно використовувати стандартний калькулятор (стандартний - маєця на увазі той який НЕ враховує який саме це прийом їжі: сніданок/обід/вечеря)
        boolean useStandardCalculator = getBooleanInput(scanner, "Чи використовувати стандартний калькулятор? (+/-): ");

        // Ініціалізація калькулятора інсуліну з введеними даними
        InsulinCalculator calculator = new InsulinCalculator(
                totalDailyDose, targetSugarLevel, increasedInsulinRequirement,
                decreasedInsulinRequirement, breakfastRatio, lunchRatio, dinnerRatio,
                useStandardCalculator
        );

        // --- Крок 2: Введення даних для конкретного розрахунку ---
        // Введення поточного рівня цукру
        double currentSugarLevel = getDoubleInput(scanner, "Введіть поточний рівень цукру (ммоль/л): ");
        scanner.nextLine(); // Очищення буфера перед введенням тексту

        // Введення інформації про їжу (для API)
        // МАЄ БУТИ НАПИСАНИЙ ОДИН ПРОДУКТ (ОВОЧ, ФРУКТ, БЛЮДО, ТОЩО) - ВВОДИТЬСЯ НА АНГЛІЙСЬКІЙ МОВІ. Наприклад: " Red apple "!
        System.out.print("Введіть продукт: ");
        String foodItem = scanner.nextLine();
        double grams = getDoubleInput(scanner, "Введіть кількість продукту (г): ");

        // --- Крок 3: Виклик API для отримання харчової інформації ---
        NutritionalInfo info = getNutritionalInfo(foodItem, grams);
        try {
            // Вивід отриманих значень харчової інформації
            System.out.printf("Харчова інформація для %s (%g г):%n", foodItem, grams);
            System.out.printf("Білки: %.2f г%n", info.getProtein());
            System.out.printf("Жири: %.2f г%n", info.getFat());
            System.out.printf("Вуглеводи: %.2f г%n", info.getCarbohydrates());
        } catch (Exception e) {
            System.out.println("Сталася помилка: " + e.getMessage());
        }

        // Використання отриманих (від API) вуглеводів для розрахунку дози інсуліну
        double carbs = info.getCarbohydrates();

        // Визначення типу прийому їжі (сніданок, обід, вечеря), за допомогою метода
        String mealType = getMealType(scanner);

        // Обчислення загальної дози інсуліну
        double totalDose = calculator.calculateTotalDose(currentSugarLevel, carbs, mealType);

        // Виведення результату
        System.out.printf("Загальна доза інсуліну: %.2f одиниць%n", totalDose);
    }

    // --- Допоміжний метод для введення числових значень ---
    private static double getDoubleInput(Scanner scanner, String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = scanner.nextDouble();
                return value; // Успішно введене значення
            } catch (InputMismatchException e) {
                System.out.println("Некоректний ввід. Будь ласка, введіть числове значення.");
                scanner.next(); // Очищення буфера від неправильного вводу
            }
        }
    }

    // --- Допоміжний метод для введення логічного значення ---
    private static boolean getBooleanInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next();
            if (input.equals("+")) return true; // "+" означає "так"
            if (input.equals("-")) return false; // "-" означає "ні"
            System.out.println("Некоректний ввід. Будь ласка, введіть '+' або '-'.");
        }
    }

    // --- Допоміжний метод для визначення типу прийому їжі ---
    private static String getMealType(Scanner scanner) {
        while (true) {
            System.out.print("Введіть прийом їжі (B - breakfast, L - lunch, D - dinner): ");
            String mealType = scanner.next().toUpperCase();
            if ("B".equals(mealType) || "L".equals(mealType) || "D".equals(mealType)) {
                return mealType; // Повертаємо правильний тип
            } else {
                System.out.println("Некоректний ввід. Будь ласка, введіть 'B', 'L' або 'D'.");
            }
        }
    }

    // --- Метод для виклику API та отримання харчової інформації ---
    private static NutritionalInfo getNutritionalInfo(String foodItem, double grams) {
        try {
            return EdamamAPI.getNutritionalInfo(foodItem, grams); // Виклик API
        } catch (Exception e) {
            System.out.println("Помилка при отриманні харчової інформації: " + e.getMessage());
            e.printStackTrace(); // Деталі помилки
            return new NutritionalInfo(0, 0, 0); // Повернення значень за замовчуванням
        }
    }
}