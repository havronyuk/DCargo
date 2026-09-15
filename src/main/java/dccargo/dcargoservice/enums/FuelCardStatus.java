package dccargo.dcargoservice.enums;

public enum FuelCardStatus {

    ACTIVE("Действует"),
    INACTIVE("Не действует");

    private final String description;

    FuelCardStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}