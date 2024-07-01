package core;

import java.time.LocalDateTime;
import java.util.Date;

public class HeartRate extends IndicatorMeasurement{

    public HeartRate(LocalDateTime measurementDateTime, int heartRate) {
        super(measurementDateTime);
        this.heartRate = heartRate;
    }

    private final int heartRate;

    public int getHeartRate() {
        return heartRate;
    }
}
