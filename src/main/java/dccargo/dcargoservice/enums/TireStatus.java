package dccargo.dcargoservice.enums;

public enum TireStatus {

	IN_STOCK("На складе"),
    INSTALLED("Установлена"),
    IN_REPAIR("В ремонте"),
    DAMAGED("Повреждена"),
    WORN_OUT("Изношена"),
    DISPOSED("Утилизирована"),
    ARCHIVED("Архив");

    private final String description;

    TireStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
