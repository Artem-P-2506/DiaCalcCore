public class NutritionalInfo {
    // Поля для збереження даних про харчові речовини
    private double fat; // Кількість жирів у грамах
    private double protein; // Кількість білків у грамах
    private double carbohydrates; // Кількість вуглеводів у грамах

    // --- Конструктор ---
    public NutritionalInfo(double fat, double protein, double carbohydrates) {
        this.fat = fat; // Ініціалізація значення жирів
        this.protein = protein; // Ініціалізація значення білків
        this.carbohydrates = carbohydrates; // Ініціалізація значення вуглеводів
    }

    // --- Методи для отримання значень ---
    public double getFat() {
        return fat; // Повертає кількість жирів
    }

    public double getProtein() {
        return protein; // Повертає кількість білків
    }

    public double getCarbohydrates() {
        return carbohydrates; // Повертає кількість вуглеводів
    }

    // --- Метод для форматованого відображення даних ---
    @Override
    public String toString() {
        // Повертає рядок з усіма даними об'єкта у форматі JSON-подібного вигляду
        return "NutritionalInfo{" +
                "fat=" + fat +
                ", protein=" + protein +
                ", carbohydrates=" + carbohydrates +
                '}';
    }
}