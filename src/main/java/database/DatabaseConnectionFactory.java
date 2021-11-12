package database;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
    private static final String DATABASE_URL = "jdbc:sqlserver://localhost:1433;databaseName=DB_Project;";
    private static final  String DATABASE_USERNAME = "denisafilip";
    private static final  String DATABASE_PASSWORD = "PasswordDB";
    private static final BasicDataSource connectionPool = new BasicDataSource();

    static {
        connectionPool.setUrl(DATABASE_URL);
        connectionPool.setUsername(DATABASE_USERNAME);
        connectionPool.setPassword(DATABASE_PASSWORD);
    }

    private DatabaseConnectionFactory() {}

    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
}
