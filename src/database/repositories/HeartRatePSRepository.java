package database.repositories;

import core.ArterialPressure;
import core.HeartRate;
import core.repositories.HeartRateRepository;
import database.exceptions.DatabaseError;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class HeartRatePSRepository implements HeartRateRepository {
    private final String connectionUrl;
    private final Properties connectionProperties;

    public HeartRatePSRepository(String connectionUrl, Properties connectionProperties) {
        this.connectionUrl = connectionUrl;
        this.connectionProperties = connectionProperties;
    }

    @Override
    public void addHeartRate(HeartRate heartRate) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            String query = String.format("INSERT INTO HeartRate (measurementDateTime, heartRate) VALUES ('%s', %d);",
                    heartRate.getMeasurementDateTime().format(df),
                    heartRate.getHeartRate());
            statement.execute(query);
        } catch (SQLException e) {
            throw new DatabaseError("Error during addHeartRate", e);
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
    public void removeHeartRate(HeartRate heartRate) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("DELETE FROM HeartRate WHERE id = %d;", heartRate.getId());
            statement.execute(query);
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during removeHeartRate", e);
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
    public List<HeartRate> getAllHeartRates() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM HeartRate;");
            ResultSet resultSet = statement.executeQuery(query);
            List <HeartRate> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new HeartRate(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3)));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllHeartRates", e);
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
    public List<HeartRate> getHeartRateByDate(LocalDateTime date) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime startDateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
            LocalDateTime endDateTime = startDateTime.plusDays(1);
            String query = String.format("SELECT * FROM HeartRate WHERE measurementDateTime >= '%s' AND measurementDateTime < '%s';", startDateTime.format(df), endDateTime.format(df));
            ResultSet resultSet = statement.executeQuery(query);
            List <HeartRate> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new HeartRate(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3)));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getHeartRateByDate", e);
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
    public List<HeartRate> getAllOver(int level) {
        Connection connection = null;
        List <HeartRate> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM HeartRate WHERE heartRate > %d;", level);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                result.add(new HeartRate(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3)));
            }
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllOver", e);
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
        return result;
    }

    @Override
    public List<HeartRate> getAllUnder(int level) {
        Connection connection = null;
        List <HeartRate> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM HeartRate WHERE heartRate <= %d;", level);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                result.add(new HeartRate(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3)));
            }
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllUnder", e);
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
        return result;
    }
}
