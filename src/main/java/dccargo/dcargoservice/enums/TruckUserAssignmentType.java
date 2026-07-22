package dccargo.dcargoservice.enums;

public enum TruckUserAssignmentType {
	
	PERMANENT("Постоянное закрепление"),
    TEMPORARY("Временное закрепление"),
    ACTUAL("Фактическое закрепление");

    private final String description;

    TruckUserAssignmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
