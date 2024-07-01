package core.repositories;
import java.time.LocalDateTime;
import java.util.List;
import core.ArterialPressure;
import java.util.Date;

public interface ArterialPressureRepository {
    void addArterialPressure (ArterialPressure pressure);
    void removeArterialPressure (ArterialPressure pressure);
    List<ArterialPressure> getAllArterialPressure();
    List<ArterialPressure> getArterialPressureByDate(LocalDateTime date);
    List<ArterialPressure> getAllUnderLower(int level);
    List<ArterialPressure> getAllUnderUpper(int level);
    List<ArterialPressure> getAllOverLower(int level);
    List<ArterialPressure> getAllOverUpper(int level);
}
