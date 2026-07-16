package dccargo.dcargoservice.enums;

public enum  TechnicalInspectionStatus {
	
	ACTIVE("Действует"),

    EXPIRED("Истек"),

    CANCELLED("Аннулирован");

    private final String title;

    TechnicalInspectionStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
