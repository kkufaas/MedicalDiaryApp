package database.repositories;

import core.HeartRate;
import core.MedicineReminder;
import core.MedicineUnit;
import core.Task;
import core.repositories.TaskRepository;
import database.exceptions.DatabaseError;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class TaskPSRepository implements TaskRepository {

    private final String connectionUrl;
    private final Properties connectionProperties;

    public TaskPSRepository(String connectionUrl, Properties connectionProperties) {
        this.connectionUrl = connectionUrl;
        this.connectionProperties = connectionProperties;
    }

    @Override
    public void addTask(Task task) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            String query = String.format("INSERT INTO Task (measurementDateTime, name, description) VALUES ('%s', '%s', '%s');",
                    task.getMeasurementDateTime().format(df),
                    task.getName(), task.getDescription());
            statement.execute(query);
        } catch (SQLException e) {
            throw new DatabaseError("Error during addTask", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DatabaseError("Error during closing the connection");
                }
            }
        }
    }

    @Override
    public void removeTask(Task task) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("DELETE FROM Task WHERE id = %d;", task.getId());
            statement.execute(query);
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during removeTask", e);
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DatabaseError("Error during closing the connection");
                }
            }
        }
    }

    @Override
    public List<Task> getTasksByDate(LocalDateTime date) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime startDateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
            LocalDateTime endDateTime = startDateTime.plusDays(1);
            String query = String.format("SELECT * FROM Task WHERE measurementDateTime >= '%s' AND measurementDateTime < '%s';", startDateTime.format(df), endDateTime.format(df));
            ResultSet resultSet = statement.executeQuery(query);
            List <Task> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new Task(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getString(3), resultSet.getString(4)));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getTasksByDate", e);
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DatabaseError("Error during closing the connection");
                }
            }
        }
    }

    @Override
    public List<Task> getAllTasks() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM Task;");
            ResultSet resultSet = statement.executeQuery(query);
            List <Task> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new Task(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getString(3), resultSet.getString(4)));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllTasks", e);
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DatabaseError("Error during closing the connection");
                }
            }
        }
    }
}
