package ittalents.finalproject.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String DB_URL = "jdbc:mysql://";
    private static final String DB_IP = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "petshop";


    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private static DBManager ourInstance = new DBManager();

    private Connection connection;

    public static DBManager getInstance() {
        return ourInstance;
    }

    private DBManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver successfully integrated");
            this.connection = DriverManager.getConnection(DB_URL + DB_IP+":"+DB_PORT+"/"+DB_NAME, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Missing a jar?");
        } catch (SQLException e) {
            System.out.println("Error connecting to DB" + e.getMessage());
        }

    }

    public Connection getConnection() {
        return connection;
    }
}
