public class NutritionalInfo {
    private double fat;
    private double protein;
    private double carbohydrates;

    public NutritionalInfo(double fat, double protein, double carbohydrates) {
        this.fat = fat;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
    }

    // Геттеры для получения значений
    public double getFat() {
        return fat;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    @Override
    public String toString() {
        return "NutritionalInfo{" +
                "fat=" + fat +
                ", protein=" + protein +
                ", carbohydrates=" + carbohydrates +
                '}';
    }
}