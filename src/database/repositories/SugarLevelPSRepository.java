package database.repositories;

import core.HeartRate;
import core.SugarLevel;
import core.repositories.SugarLevelRepository;
import database.exceptions.DatabaseError;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SugarLevelPSRepository implements SugarLevelRepository {

    private final String connectionUrl;
    private final Properties connectionProperties;

    public SugarLevelPSRepository(String connectionUrl, Properties connectionProperties) {
        this.connectionUrl = connectionUrl;
        this.connectionProperties = connectionProperties;
    }

    @Override
    public void addSugarLevel(SugarLevel sugarLevel) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            String query = String.format("INSERT INTO SugarLevel (measurementDateTime, sugarLevel) VALUES ('%s', %f);",
                    sugarLevel.getMeasurementDateTime().format(df),
                    sugarLevel.getSugarLevel());
            statement.execute(query);
        } catch (SQLException e) {
            throw new DatabaseError("Error during addSugarLevel", e);
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
    public void removeSugarLevel(SugarLevel sugarLevel) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("DELETE FROM SugarLevel WHERE id = %d;", sugarLevel.getId());
            statement.execute(query);
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during removeSugarLevel", e);
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
    public List<SugarLevel> getAllSugarLevels() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM SugarLevel;");
            ResultSet resultSet = statement.executeQuery(query);
            List <SugarLevel> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new SugarLevel(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3)));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllSugarLevels", e);
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
    public List<SugarLevel> getSugarLevelByDate(LocalDateTime date) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime startDateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
            LocalDateTime endDateTime = startDateTime.plusDays(1);
            String query = String.format("SELECT * FROM SugarLevel WHERE measurementDateTime >= '%s' AND measurementDateTime < '%s';", startDateTime.format(df), endDateTime.format(df));
            ResultSet resultSet = statement.executeQuery(query);
            List <SugarLevel> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new SugarLevel(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3)));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllSugarLevelsByDate", e);
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
    public List<SugarLevel> getAllOver(double level) {
        Connection connection = null;
        List <SugarLevel> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM SugarLevel WHERE sugarLevel > %f;", level);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                result.add(new SugarLevel(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3)));
            }
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllOver (SugarLevel) ", e);
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
