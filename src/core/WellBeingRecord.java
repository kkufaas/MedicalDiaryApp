package core;

import java.time.LocalDateTime;
import java.util.Date;

public class WellBeingRecord extends IndicatorMeasurement {
    public WellBeingRecord(LocalDateTime dateTime, String comment, WellBeingLevel wellBeingLevel) {
        super(dateTime);
        this.comment = comment;
        this.wellBeingLevel = wellBeingLevel;
    }
    private String comment;
    private WellBeingLevel wellBeingLevel;

    public String getComment() {
        return comment;
    }

    public WellBeingLevel getWellBeingLevel() {
        return wellBeingLevel;
    }


}
