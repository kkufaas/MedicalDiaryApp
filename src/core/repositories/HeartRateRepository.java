package core.repositories;

import core.HeartRate;
import core.SugarLevel;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface HeartRateRepository {
    void addHeartRate (HeartRate heartRate);
    void removeHeartRate (HeartRate heartRate);
    List<HeartRate> getAllHeartRates();
    List<HeartRate> getHeartRateByDate(LocalDateTime date);
    List<HeartRate> getAllOver(int level);
    List<HeartRate> getAllUnder(int level);

}
