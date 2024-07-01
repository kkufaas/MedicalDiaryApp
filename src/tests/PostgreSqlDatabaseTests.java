package tests;
import core.*;
import database.repositories.PostgreSqlDataBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;


public class PostgreSqlDatabaseTests extends Assertions {
    private PostgreSqlDataBase db;
    @BeforeEach
    public void setup () {
        db = new PostgreSqlDataBase("localhost", "testDb", "postgres", "12345", 5433);
    }
    @Test
    public void initialize_CreateNewDatabase_notThrowsException () {
        assertDoesNotThrow(() -> db.initialize());
    }

    /* Adders */
    @Test
    public void add_Arterial_pressure (){
        db.initialize();
        assertDoesNotThrow(() -> db.arterialPressures().addArterialPressure(new ArterialPressure(LocalDateTime.of(2024, 1, 1, 0, 0), 120, 80)));
    }

    @Test
    public void add_Heart_Rate (){
        db.initialize();
        assertDoesNotThrow(() -> db.heartRate().addHeartRate(new HeartRate(LocalDateTime.of(2024, 1, 1, 0, 0),
                98)));
    }

    @Test
    public void add_Medicine_reminder (){
        db.initialize();
        assertDoesNotThrow(() -> db.medicineReminder().addReminder(new MedicineReminder(LocalDateTime.of(2024, 1, 1, 0, 0),
                "medicine1", 80, MedicineUnit.Tablet)));
    }

    @Test
    public void add_Sugar_Level (){
        db.initialize();
        assertDoesNotThrow(() -> db.sugarLevel().addSugarLevel(new SugarLevel(LocalDateTime.of(2024, 1, 1, 0, 0),
                24)));
    }

    @Test
    public void add_Task (){
        db.initialize();
        assertDoesNotThrow(() -> db.task().addTask(new Task(LocalDateTime.of(2024, 1, 1, 0, 0),
                "name", "description")));
    }

    @Test
    public void add_WB_Record(){
        db.initialize();
        assertDoesNotThrow(() -> db.wellBeing().addRecord(new WellBeingRecord(LocalDateTime.of(2024, 1, 1, 0, 0),
                "comment", WellBeingLevel.Good)));
    }

    /* Removers */
    @Test
    public void remove_Arterial_pressure (){
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        ArterialPressure test = new ArterialPressure(dateTime, 120, 80);
        db.arterialPressures().addArterialPressure(test);
        assertDoesNotThrow(() -> db.arterialPressures().removeArterialPressure(test));
    }

    @Test
    public void remove_Heart_Rate (){
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        HeartRate test = new HeartRate(dateTime, 100);
        db.heartRate().addHeartRate(test);
        assertDoesNotThrow(() -> db.heartRate().removeHeartRate(test));
    }

    @Test
    public void remove_Medicine_reminder(){
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 1, 0);
        MedicineReminder test = new MedicineReminder(dateTime, "medicine2", 80, MedicineUnit.Ml);
        db.medicineReminder().addReminder(test);
        assertDoesNotThrow(() -> db.medicineReminder().removeReminder(test));
    }

    @Test
    public void remove_Sugar_Level(){
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 1, 0);
        SugarLevel test = new SugarLevel(dateTime, 100);
        db.sugarLevel().addSugarLevel(test);
        assertDoesNotThrow(() -> db.sugarLevel().removeSugarLevel(test));
    }

    @Test
    public void remove_Task(){
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 1, 0);
        Task test = new Task(dateTime, "TaskName", "TaskDescription");
        db.task().addTask(test);
        assertDoesNotThrow(() -> db.task().removeTask(test));
    }

    @Test
    public void remove_WBRecord(){
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 2, 1, 0);
        WellBeingRecord test = new WellBeingRecord(dateTime, "Comment", WellBeingLevel.Regular);
        db.wellBeing().addRecord(test);
        assertDoesNotThrow(() -> db.wellBeing().removeRecord(test));
    }

    /* Getters AP */
    @Test
    public void get_AllArterialPressure() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 1, 2, 0);
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime, 120, 80));
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime2, 110, 70));
        List<ArterialPressure> arterialPressures = db.arterialPressures().getAllArterialPressure();
        assertEquals(2, arterialPressures.size());
    }

    @Test
    public void get_ArterialPressure_byDate () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 1, 2, 0);
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime, 120, 80));
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime2, 110, 70));
        List<ArterialPressure> arterialPressures = db.arterialPressures().getArterialPressureByDate(dateTime);
        assertEquals(2, arterialPressures.size());
    }

    @Test
    public void get_AllUnderLower() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime, 120, 80));
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime2, 110, 70));
        List<ArterialPressure> arterialPressures = db.arterialPressures().getAllUnderLower(75);
        assertEquals(1, arterialPressures.size());
    }

    @Test
    public void get_AllUnderUpper() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime, 120, 80));
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime2, 110, 70));
        List<ArterialPressure> arterialPressures = db.arterialPressures().getAllUnderUpper(115);
        assertEquals(1, arterialPressures.size());
    }

    @Test
    public void get_AllOverLower() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime, 120, 80));
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime2, 110, 70));
        List<ArterialPressure> arterialPressures = db.arterialPressures().getAllOverLower(75);
        assertEquals(1, arterialPressures.size());
    }

    @Test
    public void get_AllOverUpper() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime, 120, 80));
        db.arterialPressures().addArterialPressure(new ArterialPressure(dateTime2, 110, 70));
        List<ArterialPressure> arterialPressures = db.arterialPressures().getAllOverUpper(115);
        assertEquals(1, arterialPressures.size());
    }

    /* Getters HR */
    @Test
    public void get_AllHeartRates() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 1, 2, 0);
        db.heartRate().addHeartRate(new HeartRate(dateTime, 90));
        db.heartRate().addHeartRate(new HeartRate(dateTime2, 70));
        List<HeartRate> heartRates = db.heartRate().getAllHeartRates();
        assertEquals(2, heartRates.size());
    }

    @Test
    public void get_HeartRateByDate () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 1, 2, 0);
        db.heartRate().addHeartRate(new HeartRate(dateTime, 90));
        db.heartRate().addHeartRate(new HeartRate(dateTime2, 70));
        List<HeartRate> heartRates = db.heartRate().getHeartRateByDate(dateTime);
        assertEquals(2, heartRates.size());
    }

    @Test
    public void get_AllOver() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 10, 2, 2, 0);
        db.heartRate().addHeartRate(new HeartRate(dateTime, 90));
        db.heartRate().addHeartRate(new HeartRate(dateTime2, 70));
        List<HeartRate> heartRates = db.heartRate().getAllOver(75);
        assertEquals(1, heartRates.size());
    }

    @Test
    public void getAllUnder() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 10, 2, 2, 0);
        db.heartRate().addHeartRate(new HeartRate(dateTime, 90));
        db.heartRate().addHeartRate(new HeartRate(dateTime2, 70));
        List<HeartRate> heartRates = db.heartRate().getAllUnder(75);
        assertEquals(1, heartRates.size());
    }

    /* Getters SR */

    @Test
    public void get_SugarLevelByDate () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 2, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.sugarLevel().addSugarLevel(new SugarLevel(dateTime, 80));
        db.sugarLevel().addSugarLevel(new SugarLevel(dateTime2, 90));
        List<SugarLevel> sugarLevels = db.sugarLevel().getSugarLevelByDate(dateTime);
        assertEquals(2, sugarLevels .size());
    }

    /* Getters SL */
    @Test
    public void get_AllSugarLevels() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 1, 2, 0);
        db.sugarLevel().addSugarLevel(new SugarLevel(dateTime, 80));
        db.sugarLevel().addSugarLevel(new SugarLevel(dateTime2, 90));
        List<SugarLevel> sugarLevels = db.sugarLevel().getAllSugarLevels();
        assertEquals(2, sugarLevels.size());
    }

    @Test
    public void get_getAllOver() {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 10, 2, 2, 0);
        db.sugarLevel().addSugarLevel(new SugarLevel(dateTime, 80));
        db.sugarLevel().addSugarLevel(new SugarLevel(dateTime2, 90));
        List<SugarLevel> sugarLevels  = db.sugarLevel().getAllOver(85);
        assertEquals(1, sugarLevels.size());
    }


    /* Getters MR */

    @Test
    public void get_Reminder_byDate () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 2, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.medicineReminder().addReminder(new MedicineReminder(dateTime, "medicine", 80, MedicineUnit.Mg));
        db.medicineReminder().addReminder(new MedicineReminder(dateTime2, "medicine", 100, MedicineUnit.Mg));
        List<MedicineReminder> medicineReminders = db.medicineReminder().getReminderByDate(dateTime);
        assertEquals(2, medicineReminders.size());
    }

    @Test
    public void get_AllReminders () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 1, 2, 0);
        db.medicineReminder().addReminder(new MedicineReminder(dateTime, "medicine", 80, MedicineUnit.Mg));
        db.medicineReminder().addReminder(new MedicineReminder(dateTime2, "medicine", 100, MedicineUnit.Mg));
        List<MedicineReminder> medicineReminders  = db.medicineReminder().getAllReminders();
        assertEquals(2, medicineReminders.size());
    }

    /* Getters Task */
    @Test
    public void get_TaskByDate () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 2, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.task().addTask(new Task(dateTime, "name", "description"));
        db.task().addTask(new Task(dateTime2, "name2", "description2"));
        List<Task> tasks = db.task().getTasksByDate(dateTime);
        assertEquals(2, tasks.size());
    }

    @Test
    public void get_AllTasks () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 1, 2, 0);
        db.task().addTask(new Task(dateTime, "name", "description"));
        db.task().addTask(new Task(dateTime2, "name2", "description2"));
        List<Task> tasks  = db.task().getAllTasks();
        assertEquals(2, tasks.size());
    }


    /* Getters WBR */
    @Test
    public void get_RecordByDate () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 2, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.wellBeing().addRecord(new WellBeingRecord(dateTime, "name", WellBeingLevel.VeryGood));
        db.wellBeing().addRecord(new WellBeingRecord(dateTime2, "name2", WellBeingLevel.VeryGood));
        List<WellBeingRecord> records = db.wellBeing().getRecordsByDate(dateTime);
        assertEquals(2, records.size());
    }

    @Test
    public void get_AllRecords () {
        db.initialize();
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 2, 1, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 2, 0);
        db.wellBeing().addRecord(new WellBeingRecord(dateTime, "name", WellBeingLevel.VeryGood));
        db.wellBeing().addRecord(new WellBeingRecord(dateTime2, "name2", WellBeingLevel.VeryGood));
        List<WellBeingRecord> records  = db.wellBeing().getAllRecords();
        assertEquals(2, records.size());
    }

    @AfterEach
    public void delete () {
        db.deleteDatabase();
    }
}
