public class InsulinCalculator {
    private double sensitivity;
    private double carbRatio;
    private double targetSugarLevel;
    private double totalDailyDose;

    // Індивідуальні коефіцієнти інсуліну на 1 ХЕ для різних прийомів їжі
    private double breakfastRatio;
    private double lunchRatio;
    private double dinnerRatio;
    private boolean useStandardCalculator; // Враховувати прийом їжі чи ні

    public InsulinCalculator(double totalDailyDose, double targetSugarLevel, boolean increasedInsulinRequirement,
                             boolean decreasedInsulinRequirement, double breakfastRatio, double lunchRatio,
                             double dinnerRatio, boolean useStandardCalculator) {
        this.totalDailyDose = totalDailyDose;
        this.targetSugarLevel = targetSugarLevel;
        this.useStandardCalculator = useStandardCalculator;
        this.breakfastRatio = breakfastRatio;
        this.lunchRatio = lunchRatio;
        this.dinnerRatio = dinnerRatio;

        // Використання правил для обчислення чутливості та стандартного углеводного коефіцієнта
        this.sensitivity = increasedInsulinRequirement ? 83 / totalDailyDose : 100 / totalDailyDose;
        this.carbRatio = decreasedInsulinRequirement ? 500 / totalDailyDose : 450 / totalDailyDose;
    }

    // Розрахунок коригуючої дози інсуліну
    public double calculateCorrectionDose(double currentSugarLevel) {
        double sugarDifference = currentSugarLevel - targetSugarLevel;
        return sugarDifference / sensitivity;
    }

    // Розрахунок дози на вуглеводи з урахуванням прийому їжі
    public double calculateCarbDose(double carbs, String mealType) {
        double ratio;

        if (useStandardCalculator) {
            // Стандартний розрахунок без прив'язки до прийому їжі
            ratio = carbRatio;
        } else {
            // Розрахунок на основі прийому їжі
            switch (mealType) {
                case "B":
                    ratio = carbRatio * breakfastRatio;
                    break;
                case "L":
                    ratio = carbRatio * lunchRatio;
                    break;
                case "D":
                    ratio = carbRatio * dinnerRatio;
                    break;
                default:
                    throw new IllegalArgumentException("Невірний тип прийому їжі");
            }
        }

        return carbs / ratio;
    }

    // Розрахунок загальної дози інсуліну
    public double calculateTotalDose(double currentSugarLevel, double carbs, String mealType) {
        return calculateCorrectionDose(currentSugarLevel) + calculateCarbDose(carbs, mealType);
    }
}