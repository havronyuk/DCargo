package dccargo.dcargoservice.enums;

public enum MileageSource {
	
	MANUAL("Вручную"),
    GPS("GPS"),
    ODOMETER("Одометр"),
    SERVICE("Техническое обслуживание"),
    IMPORT("Импорт"),
    API("Внешняя система");

    private final String description;

    MileageSource(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
