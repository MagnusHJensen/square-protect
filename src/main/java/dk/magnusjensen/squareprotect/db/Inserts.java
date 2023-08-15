package dk.magnusjensen.squareprotect.db;

import dk.magnusjensen.squareprotect.db.actions.BlockAction;
import dk.magnusjensen.squareprotect.db.actions.SessionAction;
import dk.magnusjensen.squareprotect.utils.Cache;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class relies on an already established connection being passed.
 */
public class Inserts {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void insertSession(Player player, SessionAction action, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO session (uuid, world, x, y, z, action, time) VALUES (?, ?, ?, ?, ?, ?, ?);");
        preparedStatement.setString(1, player.getUUID().toString());
        preparedStatement.setInt(2, Cache.DIMENSIONS.get(player.level().dimension().location()));
        preparedStatement.setInt(3, player.blockPosition().getX());
        preparedStatement.setInt(4, player.blockPosition().getY());
        preparedStatement.setInt(5, player.blockPosition().getZ());
        preparedStatement.setInt(6, action.ordinal());
        preparedStatement.setLong(7, System.currentTimeMillis());

        preparedStatement.executeUpdate();
        LOGGER.debug("Inserted session " + (action == SessionAction.JOIN ? "join" : "leave") + " for " + player.getScoreboardName() + " into database");
        preparedStatement.close();
    }

}
