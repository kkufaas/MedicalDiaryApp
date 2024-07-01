package core.repositories;

import core.WellBeingRecord;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface WellBeingRecordRepository {
    void addRecord(WellBeingRecord record);
    void removeRecord(WellBeingRecord record);
    List<WellBeingRecord> getRecordsByDate(LocalDateTime date);
    List<WellBeingRecord> getAllRecords();
}
