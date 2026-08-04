package dccargo.dcargoservice.model.dcargo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "sensor_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sensor_record")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long idSensorRecord;

    @Column(name = "id_order")
    private Long idOrder; // айди связи с заказом

    @Column(name = "first_digital_sensor", precision = 12, scale = 4)
    private BigDecimal firstDigitalSensor; // первое значение датчика

    @Column(name = "last_datepoint")
    private LocalDateTime lastDatepoint; // последняя точка времени

    @Column(name = "sum_distance", precision = 12, scale = 4)
    private BigDecimal sumDistance; // общее расстояние

    @Column(name = "last_address", columnDefinition = "TEXT")
    private String lastAddress; // адрес последней точки

    @Column(name = "unit_id")
    private String unitId; // UUID устройства

    @Column(name = "last_digital_sensor", precision = 12, scale = 4)
    private BigDecimal lastDigitalSensor; // последнее значение датчика

    @Column(name = "trailer")
    private String trailer; // прицеп (может быть "-")

    @Column(name = "parking_coordinate", columnDefinition = "TEXT")
    private String parkingCoordinate; // координаты стоянки

    @Column(name = "row_number")
    private Integer rowNumber; // № – номер строки

    @Column(name = "first_datepoint")
    private LocalDateTime firstDatepoint; // первая точка времени

    @Column(name = "finish_fillings", precision = 12, scale = 4)
    private BigDecimal finishFillings; // заправки на конец

    @Column(name = "sum_fillings", precision = 12, scale = 4)
    private BigDecimal sumFillings; // итоговые заправки

    @Column(name = "end_latlon_digital_sensors", columnDefinition = "TEXT")
    private String endLatlonDigitalSensors; // конечные координаты

    @Column(name = "start_latlon_digital_sensors", columnDefinition = "TEXT")
    private String startLatlonDigitalSensors; // начальные координаты

    @Column(name = "end_time_digital_sensors")
    private LocalDateTime endTimeDigitalSensors; // время конца (может быть "-" – тогда замените на String)

    @Column(name = "sensor_first_value_temperature", precision = 12, scale = 4)
    private BigDecimal sensorFirstValueTemperature; // температура

    @Column(name = "max_speed_kph", precision = 12, scale = 4)
    private BigDecimal maxSpeedKph; // макс. скорость (км/ч)

    @Column(name = "num_shop")
    private String numShop; // номер магазина (в Excel строка)

    @Column(name = "sum_datepoint")
    private LocalTime sumDatepoint; // сумма времени (интервал)

    @Column(name = "avg_speed_kph", precision = 12, scale = 4)
    private BigDecimal avgSpeedKph; // средняя скорость

    @Column(name = "unit_name")
    private String unitName; // название устройства

    @Column(name = "driver")
    private String driver; // водитель

    @Column(name = "first_address", columnDefinition = "TEXT")
    private String firstAddress; // адрес первой точки

    @Column(name = "first_fillings", precision = 12, scale = 4)
    private BigDecimal firstFillings; // заправки на начало

    @Column(name = "start_time_digital_sensors")
    private LocalDateTime startTimeDigitalSensors; // время начала (может быть "-" – тогда замените на String)

    @Column(name = "stucked_fillings", precision = 12, scale = 4)
    private BigDecimal stuckedFillings; // «застрявшие» заправки

    @Column(name = "sensor_name")
    private String sensorName; // название датчика

    @Column(name = "problem_distance", precision = 12, scale = 4)
    private BigDecimal problemDistance; // проблемное расстояние

    @Column(name = "match_type")
    private String matchType; // тип совпадения

    @Column(name = "is_total")
    private Boolean isTotal; // признак итоговой строки (0/1)

    @Column(name = "sensor_record_type")
    private String sensorRecordType; // тип записи (если нужно)


}