package core;
import java.time.LocalDateTime;
import java.util.Date;

public class IndicatorMeasurement extends Entity {
    private LocalDateTime measurementDateTime;

    public IndicatorMeasurement(LocalDateTime measurementDateTime) {
        this.measurementDateTime = measurementDateTime;
    }

    public LocalDateTime getMeasurementDateTime () {
        return measurementDateTime;
    }
}
