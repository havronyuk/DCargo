package dccargo.dcargoservice.dto.dcargo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO результата календаря
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverWorkDayDTO {
	
	private LocalDate date;

    private Long truckId;

    /**
     * Основной водитель, которому принадлежит график.
     */
    private Long userId;

    /**
     * Должен ли водитель работать по базовому графику.
     */
    private Boolean scheduledWorking;

    /**
     * Работает ли водитель фактически
     * после применения исключения.
     */
    private Boolean actualWorking;

    /**
     * WORKING, DAY_OFF, VACATION,
     * SICK_LEAVE или REPLACEMENT.
     */
    private String status;

    private String statusDescription;

    /**
     * Заполняется при замене.
     */
    private Long replacementUserId;

    private Boolean exceptionApplied;

    private String comment;

}
