package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConnectionConfig {
    public String host;
    public String dbName;
    public String username;
    public String password;
    public int port;

    public static ConnectionConfig read (String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(filename)));
        ConnectionConfig config = new ConnectionConfig();
        config.host = scanner.next();
        scanner.nextLine();
        config.dbName = scanner.next();
        scanner.nextLine();
        config.username = scanner.next();
        scanner.nextLine();
        config.password = scanner.next();
        scanner.nextLine();
        config.port = scanner.nextInt();
        scanner.close();
        return config;
    }
}
