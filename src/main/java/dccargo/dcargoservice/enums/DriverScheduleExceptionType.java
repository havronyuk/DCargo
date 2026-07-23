package dccargo.dcargoservice.enums;

public enum DriverScheduleExceptionType {
	
	WORKING("Рабочий день"),
    DAY_OFF("Выходной день"),
    VACATION("Отпуск"),
    SICK_LEAVE("Больничный"),
    REPLACEMENT("Замена другим водителем");

	private final String description;

	DriverScheduleExceptionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
