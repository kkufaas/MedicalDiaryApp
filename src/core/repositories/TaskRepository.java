package core.repositories;

import core.MedicineReminder;
import core.SugarLevel;
import core.Task;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TaskRepository {
    void addTask(Task task);
    void removeTask(Task task);
    List<Task> getTasksByDate(LocalDateTime date);
    List<Task> getAllTasks();
}