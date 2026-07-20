package dccargo.dcargoservice.enums;

public enum TireType {

	STEERING("Рулевая"),
    DRIVE("Ведущая"),
    TRAILER("Прицепная"),
    UNIVERSAL("Универсальная");

    private final String description;

    TireType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
