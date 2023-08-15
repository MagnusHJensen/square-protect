package dk.magnusjensen.squareprotect.listener;

import dk.magnusjensen.squareprotect.SquareProtect;
import dk.magnusjensen.squareprotect.commands.SquareProtectCommand;
import dk.magnusjensen.squareprotect.db.Database;
import dk.magnusjensen.squareprotect.listener.block.BlockBreakListener;
import dk.magnusjensen.squareprotect.listener.player.PlayerJoinListener;
import dk.magnusjensen.squareprotect.utils.Cache;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventListenerRegistrator {
    public static void registerEventListeners() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;



        // Block Events
        forgeBus.addListener(BlockBreakListener::onBlockBreak);

        // Player events
        forgeBus.addListener(PlayerJoinListener::onPlayerJoin);
        forgeBus.addListener(PlayerJoinListener::onPlayerLeave);

        // World events
        forgeBus.addListener(EventListenerRegistrator::onServerStart);

        // Events needed for mod setup
        forgeBus.addListener(EventListenerRegistrator::registerCommands);
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(SquareProtectCommand.register());
    }

    private static void onServerStart(ServerStartingEvent event) {
        SquareProtect.LOGGER.debug("Adding dimensions to database");
        try(Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR IGNORE INTO world (name) VALUES (?);");
            for (ResourceKey<Level> key : event.getServer().levelKeys()) {
                preparedStatement.setString(1, key.location().toString());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();
            connection.commit();
            ResultSet set = connection.createStatement().executeQuery("SELECT * FROM world;");
            while (set.next()) {
                Cache.DIMENSIONS.put(ResourceLocation.tryParse(set.getString("name")), set.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SquareProtect.LOGGER.debug("Successfully added dimensions to database");
    }
}
