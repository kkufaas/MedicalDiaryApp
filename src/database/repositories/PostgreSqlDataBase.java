package database.repositories;

import core.*;
import core.repositories.*;
import database.exceptions.DatabaseError;

import java.sql.*;
import java.util.Properties;

public class PostgreSqlDataBase implements DataBase {
    private final ArterialPressurePSRepository arterialPressures;
    private final HeartRatePSRepository heartRates;
    private final MedicineReminderPSRepository medicineReminders;
    private final SugarLevelPSRepository sugarLevels;
    private final TaskPSRepository tasks;
    private final WellBeingPSRepository wellBeingRecords;
    private final String host;
    private final int port;
    private final String dbName;
    private final String username;
    private final String password;

    @Override
    public void initialize() {
        try {
            if (!dataBaseExists(host, port, dbName, username, password)) {
                createNewDataBase(host, port, dbName, username, password);
            }
            createDatabaseTables(host, port, dbName, username, password);
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during database creation", e);
        }
    }

    public void deleteDatabase() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    createConnectionUrl(host, port),
                    createConnectionProperties(username, password)
            );
            Statement statement = connection.createStatement();
            statement.execute("DROP DATABASE " + dbName + ";");
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during database deletion", e);
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

    public PostgreSqlDataBase(String host, String dbName, String username, String password, int port) {
        this.host = host;
        this.port = port;
        this.dbName = dbName.toLowerCase();
        this.username = username;
        this.password = password;

        arterialPressures = new ArterialPressurePSRepository(
                createConnectionUrl(host, port, dbName.toLowerCase()),
                createConnectionProperties(username, password)
        );

        heartRates = new HeartRatePSRepository(
                createConnectionUrl(host, port, dbName.toLowerCase()),
                createConnectionProperties(username, password)
        );

        medicineReminders = new MedicineReminderPSRepository(
                createConnectionUrl(host, port, dbName.toLowerCase()),
                createConnectionProperties(username, password)
        );

        sugarLevels = new SugarLevelPSRepository(
                createConnectionUrl(host, port, dbName.toLowerCase()),
                createConnectionProperties(username, password)
        );

        tasks = new TaskPSRepository(
                createConnectionUrl(host, port, dbName.toLowerCase()),
                createConnectionProperties(username, password)
        );

        wellBeingRecords = new WellBeingPSRepository(
                createConnectionUrl(host, port, dbName.toLowerCase()),
                createConnectionProperties(username, password)
        );
    }

    private String createConnectionUrl (String host, int port, String dbName) {
        return "jdbc:postgresql://"+ host + ":" + port + "/" +
                dbName;
    }

    private Properties createConnectionProperties (String username, String password) {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        return props;
    }

    private String createConnectionUrl (String host, int port){
        return "jdbc:postgresql://"+ host + ":" + port + "/";
    }

    private boolean dataBaseExists(String host, int port, String dbName, String username, String password) throws SQLException {
        Connection connection = null;
        boolean res;
        try {
            connection = DriverManager.getConnection(
                    createConnectionUrl(host, port),
                    createConnectionProperties(username, password));
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT FROM pg_database WHERE datname = '"+dbName+"';");
            res = result.next();
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during dataBaseExists", e);
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
        return res;
    }

    private void createNewDataBase(String host, int port, String dbName, String username, String password) throws SQLException {
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(
                    createConnectionUrl(host, port),
                    createConnectionProperties(username, password));
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE " + dbName + ";");
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during create Database", e);
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

    private void createDatabaseTables(String host, int port, String dbName, String username, String password) throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    createConnectionUrl(host, port, dbName),
                    createConnectionProperties(username, password)
            );
            Statement statement = connection.createStatement();

            createTableIfNotExists(statement, "ArterialPressure", "CREATE TABLE ArterialPressure(" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "measurementDateTime TIMESTAMP NOT NULL, " +
                    "lowerPressure INTEGER NOT NULL, " +
                    "upperPressure INTEGER NOT NULL);");

            createTableIfNotExists(statement, "HeartRate", "CREATE TABLE HeartRate(" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "measurementDateTime TIMESTAMP NOT NULL, " +
                    "heartRate INTEGER NOT NULL);");

            createTableIfNotExists(statement, "MedicineReminder", "CREATE TABLE MedicineReminder(" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "reminderDateTime TIMESTAMP NOT NULL, " +
                    "medicineName TEXT NOT NULL, " +
                    "dose DOUBLE PRECISION NOT NULL, " +
                    "unit TEXT NOT NULL);");

            createTableIfNotExists(statement, "SugarLevel", "CREATE TABLE SugarLevel(" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "measurementDateTime TIMESTAMP NOT NULL, " +
                    "sugarLevel DOUBLE PRECISION NOT NULL);");

            createTableIfNotExists(statement, "Task", "CREATE TABLE Task(" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "measurementDateTime TIMESTAMP NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT NOT NULL);");

            createTableIfNotExists(statement, "WellBeingRecord", "CREATE TABLE WellBeingRecord(" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "measurementDateTime TIMESTAMP NOT NULL, " +
                    "comment TEXT NOT NULL, " +
                    "description TEXT NOT NULL);");
        }
        catch (SQLException e) {
            throw new DatabaseError("Error during creation database table", e);
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

    private void createTableIfNotExists(Statement statement, String tableName, String createTableSql) throws SQLException {
        String checkTableExistsQuery = String.format("SELECT EXISTS (" +
                "SELECT FROM information_schema.tables " +
                "WHERE table_schema = 'public' AND table_name = '%s');", tableName.toLowerCase());

        ResultSet resultSet = statement.executeQuery(checkTableExistsQuery);
        if (resultSet.next() && !resultSet.getBoolean(1)) {
            statement.execute(createTableSql);
        }
    }

    @Override
    public ArterialPressureRepository arterialPressures() {
        return arterialPressures;
    }

    @Override
    public HeartRateRepository heartRate() {
        return heartRates;
    }

    @Override
    public MedicineReminderRepository medicineReminder() {
        return medicineReminders;
    }

    @Override
    public SugarLevelRepository sugarLevel() {
        return sugarLevels;
    }

    @Override
    public TaskRepository task() {
        return tasks;
    }

    @Override
    public WellBeingRecordRepository wellBeing() {
        return wellBeingRecords;
    }
}
