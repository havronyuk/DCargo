package dccargo.dcargoservice.enums;

public enum RouteSheetStatus {


    CREATED("Cоздан"),
    PRINTED("Распечатан"),
    ACTIVE("Действует"),
    COMPLETED("Завершено"),
    EXPIRED("Истекло"),
    REASSIGNED("Переназначено"),
    CANCELLED("Отменено");

    private final String description;

    RouteSheetStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
