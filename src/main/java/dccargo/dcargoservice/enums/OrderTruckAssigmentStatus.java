package dccargo.dcargoservice.enums;

public enum OrderTruckAssigmentStatus {

    ACTIVE("Действует"),
    COMPLETED("Завершено"),
    EXPIRED("Истекло"),
    REASSIGNED("Переназначено"),
    CANCELLED("Отменено");

    private final String description;

    OrderTruckAssigmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
