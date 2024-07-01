package core.repositories;

public interface DataBase {
    ArterialPressureRepository arterialPressures();
    HeartRateRepository heartRate();
    MedicineReminderRepository medicineReminder();
    SugarLevelRepository sugarLevel();
    TaskRepository task();
    WellBeingRecordRepository wellBeing();
    void initialize();
}

