package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnector {


    private static Connection connection;


    public MysqlConnector() {
        this.initialize();

    }
    public void initialize() {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/mydatabase"; // Replace 'mydatabase' with your database name
        String user = "username"; // Replace 'username' with your MySQL username
        String password = "password"; // Replace 'password' with your MySQL password


        try {
           connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Connection failed! Error: " + e.getMessage());
        }
    }


    public Connection connection() {
        return connection;
    }
}
