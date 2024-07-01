package core.repositories;

import core.MedicineReminder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface MedicineReminderRepository {
    void addReminder(MedicineReminder record);
    void removeReminder(MedicineReminder record);
    List<MedicineReminder> getReminderByDate(LocalDateTime date);
    List<MedicineReminder> getAllReminders();
}
