package core;

import java.time.LocalDateTime;
import java.util.Date;

public class SugarLevel extends IndicatorMeasurement {

    public SugarLevel(LocalDateTime measurementDateTime, double sugarLevel) {
        super(measurementDateTime);
        this.sugarLevel = sugarLevel;
    }

    private final double sugarLevel;

    public double getSugarLevel () {
        return sugarLevel;
    }
}
