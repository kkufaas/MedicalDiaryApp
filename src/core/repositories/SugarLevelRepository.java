package core.repositories;

import core.SugarLevel;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface SugarLevelRepository {
    void addSugarLevel (SugarLevel sugarLevel);
    void removeSugarLevel (SugarLevel sugarLevel);
    List<SugarLevel> getAllSugarLevels();
    List<SugarLevel> getSugarLevelByDate(LocalDateTime date);
    List<SugarLevel> getAllOver(double level);
}
