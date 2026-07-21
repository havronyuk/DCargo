package dccargo.dcargoservice.enums;

public enum TireSeasonType {
	 	SUMMER("Летняя"),
	    WINTER("Зимняя"),
	    ALL_SEASON("Всесезонная");

	    private final String description;

	    TireSeasonType(String description) {
	        this.description = description;
	    }

	    public String getDescription() {
	        return description;
	    }

}
