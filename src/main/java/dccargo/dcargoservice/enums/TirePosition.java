package dccargo.dcargoservice.enums;

public enum TirePosition {	

    LEFT("Слева"),
    RIGHT("Справа"),
    INNER_LEFT("Внутреннее левое"),
    INNER_RIGHT("Внутреннее правое"),
    OUTER_LEFT("Наружное левое"),
    OUTER_RIGHT("Наружное правое"),
    SINGLE("Одиночное"),
	SPARE("Запасное");

    private final String description;

    TirePosition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
