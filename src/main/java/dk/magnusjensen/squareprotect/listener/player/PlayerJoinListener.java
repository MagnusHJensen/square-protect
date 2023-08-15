package dk.magnusjensen.squareprotect.listener.player;

import dk.magnusjensen.squareprotect.db.Database;
import dk.magnusjensen.squareprotect.db.Inserts;
import dk.magnusjensen.squareprotect.db.actions.SessionAction;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerJoinListener {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        LOGGER.debug("Player " + event.getEntity().getScoreboardName() + " joined the server");
        try(Connection connection = Database.getConnection()) {
            insertUsername(event, connection);
            Inserts.insertSession(event.getEntity(), SessionAction.JOIN, connection);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        LOGGER.debug("Player " + event.getEntity().getScoreboardName() + " left the server");
        try(Connection connection = Database.getConnection()) {
            Inserts.insertSession(event.getEntity(), SessionAction.LEAVE, connection);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertUsername(PlayerEvent.PlayerLoggedInEvent event, Connection connection) throws SQLException {
        ResultSet set = connection.createStatement().executeQuery("SELECT * FROM user where uuid = '" + event.getEntity().getUUID() + "' ORDER BY time DESC LIMIT 1;");
        if (set.next() && set.getString("username").equals(event.getEntity().getScoreboardName())) {
            LOGGER.debug("Skipping user " + event.getEntity().getScoreboardName() + " as they already exists in database");
        } else {
            connection.createStatement().executeUpdate("INSERT INTO user (time, username, uuid) VALUES (" + System.currentTimeMillis() + ", '" + event.getEntity().getScoreboardName() + "', '" + event.getEntity().getUUID() + "');");
        }
        set.close();
        LOGGER.debug("Inserted new user " + event.getEntity().getScoreboardName() + " into database");
    }
}
