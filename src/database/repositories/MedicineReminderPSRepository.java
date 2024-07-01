package database.repositories;

import core.ArterialPressure;
import core.HeartRate;
import core.MedicineReminder;
import core.MedicineUnit;
import core.repositories.MedicineReminderRepository;
import database.exceptions.DatabaseError;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MedicineReminderPSRepository implements MedicineReminderRepository {

    private final String connectionUrl;
    private final Properties connectionProperties;

    public MedicineReminderPSRepository(String connectionUrl, Properties connectionProperties) {
        this.connectionUrl = connectionUrl;
        this.connectionProperties = connectionProperties;
    }

    @Override
    public void addReminder(MedicineReminder record) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            String query = String.format("INSERT INTO MedicineReminder (reminderDateTime, medicineName, dose, unit) VALUES ('%s','%s', '%f', '%s');",
                    record.getReminderDateTime().format(df), record.getMedicineName(), record.getDose(), record.getUnit());
            statement.execute(query);
        } catch (SQLException e) {
            throw new DatabaseError("Error during addReminder", e);
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
    public void removeReminder(MedicineReminder record) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("DELETE FROM MedicineReminder WHERE id = %d;", record.getId());
            statement.execute(query);
        } catch (SQLException e) {
            throw new DatabaseError("Error during removeReminder", e);
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
    public List<MedicineReminder> getReminderByDate(LocalDateTime date) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime startDateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
            LocalDateTime endDateTime = startDateTime.plusDays(1);
            String query = String.format("SELECT * FROM MedicineReminder WHERE reminderDateTime >= '%s' AND reminderDateTime < '%s';",
                    startDateTime.format(df), endDateTime.format(df));
            ResultSet resultSet = statement.executeQuery(query);
            List<MedicineReminder> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new MedicineReminder(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getString(3), resultSet.getDouble(4), MedicineUnit.valueOf(resultSet.getString(5))));
            }
            return result;
        }
        catch (SQLException e){
            throw new DatabaseError("Error during getArterialPressureByDate", e);
        }
        finally {
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
    public List<MedicineReminder> getAllReminders() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM MedicineReminder;");
            ResultSet resultSet = statement.executeQuery(query);
            List <MedicineReminder> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new MedicineReminder(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getString(3), resultSet.getDouble(4), MedicineUnit.valueOf(resultSet.getString(5))));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllReminders", e);
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
