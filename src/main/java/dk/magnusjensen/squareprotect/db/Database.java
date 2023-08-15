package dk.magnusjensen.squareprotect.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        config.setJdbcUrl("jdbc:sqlite:./squareprotect.db");
        config.setUsername("admin");
        config.setPassword("password");
        config.setAutoCommit(false);

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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS block (actor VARCHAR(25), world INTEGER REFERENCES world(id), x INTEGER, y INTEGER, z INTEGER, blockType VARCHAR(100), blockState TEXT, action INTEGER, time INTEGER);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS user (username VARCHAR(16), uuid VARCHAR(40), time INTEGER);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS session (uuid VARCHAR(40) REFERENCES user(uuid), world INTEGER REFERENCES world(id), x INTEGER, y INTEGER, z INTEGER, action INTEGER NOT NULL CHECK ( action >= 0 AND action <= 2) , time INTEGER);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS world (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) UNIQUE);");
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
