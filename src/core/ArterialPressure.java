package core;

import java.time.LocalDateTime;
import java.util.Date;

public class ArterialPressure extends IndicatorMeasurement {

    public ArterialPressure(LocalDateTime measurementDateTime, int upperPressure, int lowerPressure) {
        super(measurementDateTime);
        this.upperPressure = upperPressure;
        this.lowerPressure = lowerPressure;
    }

    private final int upperPressure;
    private final int lowerPressure;

    public int getUpperPressure () {
        return upperPressure;
    }

    public int getLowerPressure () {
        return lowerPressure;
    }
}
