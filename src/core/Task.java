package core;

import java.time.LocalDateTime;
import java.util.Date;

public class Task extends IndicatorMeasurement {
    public Task(LocalDateTime dateTime, String name, String description) {
        super(dateTime);
        this.name = name;
        this.description = description;
    }
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
