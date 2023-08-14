package dk.magnusjensen.squareprotect.listener.block;


import dk.magnusjensen.squareprotect.db.Database;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.sql.Connection;
import java.sql.SQLException;

public class BlockBreakListener {
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        System.out.println("Block broken " + event.getPos());
        try (Connection connection = Database.getConnection()) {
            connection.createStatement().executeUpdate("INSERT INTO block (time, username, x, y, z, action) VALUES (" + System.currentTimeMillis() + ", '" + event.getPlayer().getScoreboardName() + "', " + event.getPos().getX() + ", " + event.getPos().getY() + ", " + event.getPos().getZ() + ", 0);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
