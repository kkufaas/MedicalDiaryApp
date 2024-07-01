package database.repositories;

import core.ArterialPressure;
import core.repositories.ArterialPressureRepository;
import database.exceptions.DatabaseError;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class ArterialPressurePSRepository implements ArterialPressureRepository {

    private final String connectionUrl;
    private final Properties connectionProperties;

    public ArterialPressurePSRepository(String connectionUrl, Properties connectionProperties) {
        this.connectionUrl = connectionUrl;
        this.connectionProperties = connectionProperties;
    }

    @Override
    public void addArterialPressure(ArterialPressure pressure) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            String query = String.format("INSERT INTO ArterialPressure (measurementDateTime, upperPressure, lowerPressure) VALUES ('%s', %d, %d);",
                    pressure.getMeasurementDateTime().format(df), pressure.getUpperPressure(),
                    pressure.getLowerPressure());
            statement.execute(query);
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during addArterialPressure", e);
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
    public void removeArterialPressure(ArterialPressure pressure) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("DELETE FROM ArterialPressure WHERE id = %d;", pressure.getId());
            statement.execute(query);
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during removeArterialPressure", e);
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
    public List<ArterialPressure> getAllArterialPressure() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM ArterialPressure;");
            ResultSet resultSet = statement.executeQuery(query);
            List <ArterialPressure> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new ArterialPressure(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3), resultSet.getInt(4)));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllArterialPressure", e);
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
    public List<ArterialPressure> getArterialPressureByDate(LocalDateTime date) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime startDateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
            LocalDateTime endDateTime = startDateTime.plusDays(1);
            String query = String.format("SELECT * FROM ArterialPressure WHERE measurementDateTime >= '%s' AND measurementDateTime < '%s';", startDateTime.format(df), endDateTime.format(df));
            ResultSet resultSet = statement.executeQuery(query);
            List <ArterialPressure> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new ArterialPressure(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3), resultSet.getInt(4)));
            }
            return result;
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getArterialPressureByDate", e);
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
    public List<ArterialPressure> getAllUnderLower(int level) {
        Connection connection = null;
        List <ArterialPressure> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM ArterialPressure WHERE lowerPressure <= %d;", level);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                result.add(new ArterialPressure(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3), resultSet.getInt(4)));
            }
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllUnderLower", e);
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
    public List<ArterialPressure> getAllUnderUpper(int level) {
        Connection connection = null;
        List <ArterialPressure> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM ArterialPressure WHERE upperPressure <= %d;", level);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                result.add(new ArterialPressure(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3), resultSet.getInt(4)));
            }
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllUnderUpper", e);
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
    public List<ArterialPressure> getAllOverLower(int level) {
        Connection connection = null;
        List <ArterialPressure> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM ArterialPressure WHERE lowerPressure >= %d;", level);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                result.add(new ArterialPressure(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3), resultSet.getInt(4)));
            }
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllOverLower", e);
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
    public List<ArterialPressure> getAllOverUpper(int level) {
        Connection connection = null;
        List <ArterialPressure> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionProperties);
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM ArterialPressure WHERE upperPressure >= %d;", level);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                result.add(new ArterialPressure(resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getInt(3), resultSet.getInt(4)));
            }
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during getAllOverUpper", e);
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
