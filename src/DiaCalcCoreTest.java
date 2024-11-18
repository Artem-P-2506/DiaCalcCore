import java.util.InputMismatchException;
import java.util.Scanner;

public class DiaCalcCoreTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Введення основних даних
        double totalDailyDose = getDoubleInput(scanner, "Введіть загальну добову дозу інсуліну: ");
        double targetSugarLevel = getDoubleInput(scanner, "Введіть цільовий рівень цукру в крові (ммоль/л): ");

        boolean increasedInsulinRequirement = getBooleanInput(scanner, "Чи є підвищена потреба в інсуліні? (+/-): ");
        boolean decreasedInsulinRequirement = !increasedInsulinRequirement ? getBooleanInput(scanner, "Чи є знижена потреба в інсуліні? (+/-): ") : false;

        double breakfastRatio = getDoubleInput(scanner, "Скільки інсуліну потрібно на 1 ХЕ на сніданок: ");
        double lunchRatio = getDoubleInput(scanner, "Скільки інсуліну потрібно на 1 ХЕ на обід: ");
        double dinnerRatio = getDoubleInput(scanner, "Скільки інсуліну потрібно на 1 ХЕ на вечерю: ");

        boolean useStandardCalculator = getBooleanInput(scanner, "Чи використовувати стандартний калькулятор? (+/-): ");

        InsulinCalculator calculator = new InsulinCalculator(
                totalDailyDose, targetSugarLevel, increasedInsulinRequirement,
                decreasedInsulinRequirement, breakfastRatio, lunchRatio, dinnerRatio,
                useStandardCalculator
        );

        // Введення даних для розрахунку дози інсуліну
        double currentSugarLevel = getDoubleInput(scanner, "Введіть поточний рівень цукру (ммоль/л): ");
        scanner.nextLine(); // Очищає буфер
        System.out.print("Введіть продукт: ");
        String foodItem = scanner.nextLine();
        double grams = getDoubleInput(scanner, "Введіть кількість продукту (г): ");

        // Виклик API для отримання харчової інформації
        NutritionalInfo info = getNutritionalInfo(foodItem, grams);
        try {
            // Вивід отриманих значень
            System.out.printf("Харчова інформація для %s (%g г):%n", foodItem, grams);
            System.out.printf("Білки: %.2f г%n", info.getProtein());
            System.out.printf("Жири: %.2f г%n", info.getFat());
            System.out.printf("Вуглеводи: %.2f г%n", info.getCarbohydrates());
        } catch (Exception e) {
            System.out.println("Сталася помилка: " + e.getMessage());
        } finally {
            scanner.close(); // Закриваємо сканер
        }
        double carbs = info.getCarbohydrates(); // Використовуємо вуглеводи для розрахунку дози

        String mealType = getMealType(scanner);
        double totalDose = calculator.calculateTotalDose(currentSugarLevel, carbs, mealType);

        System.out.printf("Загальна доза інсуліну: %.2f одиниць%n", totalDose);
        scanner.close();
    }

    private static double getDoubleInput(Scanner scanner, String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = scanner.nextDouble();
                return value; // Повертаємо значення, якщо ввід успішний
            } catch (InputMismatchException e) {
                System.out.println("Некоректний ввід. Будь ласка, введіть числове значення.");
                scanner.next(); // Очищення неправильного вводу
            }
        }
    }

    private static boolean getBooleanInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next();
            if (input.equals("+")) return true;
            if (input.equals("-")) return false;
            System.out.println("Некоректний ввід. Будь ласка, введіть '+' або '-'.");
        }
    }

    private static String getMealType(Scanner scanner) {
        while (true) {
            System.out.print("Введіть прийом їжі (B - breakfast, L - lunch, D - dinner): ");
            String mealType = scanner.next().toUpperCase();
            if ("B".equals(mealType) || "L".equals(mealType) || "D".equals(mealType)) {
                return mealType; // Повертаємо правильний тип прийому їжі
            }
            else {
                System.out.println("Некоректний ввід. Будь ласка, введіть 'B', 'L' або 'D'.");
            }
        }
    }

    private static NutritionalInfo getNutritionalInfo(String foodItem, double grams) {
        try {
            return EdamamAPI.getNutritionalInfo(foodItem, grams); // Отримуємо харчову інформацію
        } catch (Exception e) {
            System.out.println("Помилка при отриманні харчової інформації: " + e.getMessage());
            e.printStackTrace(); // Виводимо деталі помилки
            return new NutritionalInfo(0, 0, 0); // Повертаємо нульові значення вуглеводів
        }
    }
}