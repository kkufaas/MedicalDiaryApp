package database.repositories;

import core.HeartRate;
import core.WellBeingLevel;
import core.WellBeingRecord;
import core.repositories.WellBeingRecordRepository;
import database.exceptions.DatabaseError;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class WellBeingPSRepository implements WellBeingRecordRepository {
    private final String connectionUrl;
    private final Properties connectionProperties;

    public WellBeingPSRepository(String connectionUrl, Properties connectionProperties) {
        this.connectionUrl = connectionUrl;
        this.connectionProperties = connectionProperties;
    }

    @Override
    public void addRecord(WellBeingRecord record) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            String query = String.format("INSERT INTO WellBeingRecord (measurementDateTime, comment, description) VALUES ('%s', '%s', '%s');",
                    record.getMeasurementDateTime().format(df),
                    record.getComment(), record.getWellBeingLevel());
            statement.execute(query);
        } catch (SQLException e) {
            throw new DatabaseError("Error during addWellBeingLevel", e);
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
    public void removeRecord(WellBeingRecord record) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("DELETE FROM WellBeingRecord WHERE id = %d;", record.getId());
            statement.execute(query);
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during removeWBRecord", e);
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
    public List<WellBeingRecord> getRecordsByDate(LocalDateTime date) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime startDateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
            LocalDateTime endDateTime = startDateTime.plusDays(1);
            String query = String.format("SELECT * FROM WellBeingRecord WHERE measurementDateTime >= '%s' AND measurementDateTime < '%s';", startDateTime.format(df), endDateTime.format(df));
            ResultSet resultSet = statement.executeQuery(query);
            List <WellBeingRecord> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new WellBeingRecord(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getString(3), WellBeingLevel.valueOf(resultSet.getString(4))));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getRecordsByDate", e);
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
    public List<WellBeingRecord> getAllRecords() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM WellBeingRecord;");
            ResultSet resultSet = statement.executeQuery(query);
            List <WellBeingRecord> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new WellBeingRecord(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getString(3), WellBeingLevel.valueOf(resultSet.getString(4))));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllHeartRecords", e);
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
