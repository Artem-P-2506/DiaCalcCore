public class InsulinCalculator {
    // Основні параметри розрахунку
    private double sensitivity; // Чутливість до інсуліну (скільки ммоль/л знижує 1 одиниця інсуліну)
    private double carbRatio; // Стандартний вуглеводний коефіцієнт (скільки г вуглеводів на 1 одиницю інсуліну)
    private double targetSugarLevel; // Цільовий рівень цукру
    private double totalDailyDose; // Загальна добова доза інсуліну

    // Індивідуальні коефіцієнти для різних прийомів їжі
    private double breakfastRatio; // Коефіцієнт для сніданку
    private double lunchRatio; // Коефіцієнт для обіду
    private double dinnerRatio; // Коефіцієнт для вечері
    private boolean useStandardCalculator; // Чи використовувати стандартний розрахунок (без прив'язки до прийому їжі)

    // --- Конструктор ---
    public InsulinCalculator(double totalDailyDose, double targetSugarLevel, boolean increasedInsulinRequirement,
                             boolean decreasedInsulinRequirement, double breakfastRatio, double lunchRatio,
                             double dinnerRatio, boolean useStandardCalculator) {
        this.totalDailyDose = totalDailyDose;
        this.targetSugarLevel = targetSugarLevel;
        this.useStandardCalculator = useStandardCalculator;
        this.breakfastRatio = breakfastRatio;
        this.lunchRatio = lunchRatio;
        this.dinnerRatio = dinnerRatio;

        // Обчислення чутливості та вуглеводного коефіцієнта на основі стану пацієнта
        this.sensitivity = increasedInsulinRequirement ? 83 / totalDailyDose : 100 / totalDailyDose;
        this.carbRatio = decreasedInsulinRequirement ? 500 / totalDailyDose : 450 / totalDailyDose;
        // Чутливість визначає, наскільки одиниця інсуліну знижує рівень цукру.
        // Вуглеводний коефіцієнт визначає, скільки вуглеводів обробляє 1 одиниця інсуліну.
    }

    // --- Розрахунок коригуючої дози ---
    public double calculateCorrectionDose(double currentSugarLevel) {
        double sugarDifference = currentSugarLevel - targetSugarLevel; // Наскільки поточний рівень цукру перевищує цільовий
        return sugarDifference / sensitivity; // Чутливість використовується для розрахунку потрібної корекції
    }

    // --- Розрахунок дози на вуглеводи ---
    public double calculateCarbDose(double carbs, String mealType) {
        double ratio; // Коефіцієнт для розрахунку дози інсуліну на вуглеводи

        if (useStandardCalculator) {
            // Стандартний підхід: не враховує специфіку прийомів їжі
            ratio = carbRatio;
        } else {
            // Врахування індивідуальних коефіцієнтів для кожного прийому їжі
            switch (mealType) {
                case "B": // Сніданок
                    ratio = carbRatio * breakfastRatio;
                    break;
                case "L": // Обід
                    ratio = carbRatio * lunchRatio;
                    break;
                case "D": // Вечеря
                    ratio = carbRatio * dinnerRatio;
                    break;
                default:
                    throw new IllegalArgumentException("Невірний тип прийому їжі");
                    // Якщо тип прийому їжі невідомий, викидається виняток.
            }
        }

        return carbs / ratio; // Розрахунок дози інсуліну на основі вуглеводів і коефіцієнта
    }

    // --- Розрахунок загальної дози ---
    public double calculateTotalDose(double currentSugarLevel, double carbs, String mealType) {
        // Загальна доза складається з коригуючої дози та дози на вуглеводи
        return calculateCorrectionDose(currentSugarLevel) + calculateCarbDose(carbs, mealType);
    }
}