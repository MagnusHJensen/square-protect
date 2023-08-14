package dk.magnusjensen.squareprotect.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        config.setJdbcUrl("jdbc:sqlite:./squareprotect.db");
        config.setUsername("admin");
        config.setPassword("password");

        ds = new HikariDataSource(config);
    }

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void createDatabaseTables() {
        try (Connection connection = Database.getConnection()){
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS block (time INTEGER, user INTEGER, x INTEGER, y INTEGER, z INTEGER, action INTEGER);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS user (time INTEGER, username VARCHAR(16), uuid VARCHAR(40));");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
