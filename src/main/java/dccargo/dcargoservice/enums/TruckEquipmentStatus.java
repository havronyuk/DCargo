package dccargo.dcargoservice.enums;

public enum TruckEquipmentStatus {
	
	ACTIVE("Эксплуатируется"),
	IN_REPAIR("В ремонте"),
    REMOVED("Снято"),
    BROKEN("Неисправно"),
    ARCHIVED("Архив");

    private final String description;

    TruckEquipmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
