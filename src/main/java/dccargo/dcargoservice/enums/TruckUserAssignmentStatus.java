package dccargo.dcargoservice.enums;

public enum TruckUserAssignmentStatus {
	
	ACTIVE("Действует"),
    COMPLETED("Завершено"),
    EXPIRED("Истекло"),
    CANCELLED("Отменено");

    private final String description;

    TruckUserAssignmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
