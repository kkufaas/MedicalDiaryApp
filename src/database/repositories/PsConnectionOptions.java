package database.repositories;

public class PsConnectionOptions {
    private final String username;
    private final String password;
    private final int port;
    private final String host;
    private final String database;


    public PsConnectionOptions (String username, String password, int port, String host, String database){
        this.username = username;
        this.password = password;
        this.port = port;
        this.host = host;
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getHost (){
        return host;
    }

    public String getDatabase() {
        return database;
    }
}
