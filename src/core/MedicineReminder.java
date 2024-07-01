package core;

import java.time.LocalDateTime;
import java.util.Date;

public class MedicineReminder extends Entity{
    private LocalDateTime reminderDateTime;
    private String medicineName;
    private double dose;
    private MedicineUnit unit;

    public LocalDateTime getReminderDateTime() {
        return reminderDateTime;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public double getDose() {
        return dose;
    }

    public MedicineUnit getUnit() {
        return unit;
    }

    public MedicineReminder(LocalDateTime reminderDateTime, String medicineName, double dose, MedicineUnit unit) {
        this.reminderDateTime = reminderDateTime;
        this.medicineName = medicineName;
        this.dose = dose;
        this.unit = unit;
    }
}
