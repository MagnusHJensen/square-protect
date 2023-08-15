package dk.magnusjensen.squareprotect.listener.block;


import com.mojang.serialization.DataResult;
import dk.magnusjensen.squareprotect.db.actions.BlockAction;
import dk.magnusjensen.squareprotect.db.Database;
import dk.magnusjensen.squareprotect.utils.Cache;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BlockBreakListener {
    // Add static final logger
    private static final Logger LOGGER = LogManager.getLogger();
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        try (Connection connection = Database.getConnection()) {
            LOGGER.debug("Block break event received");
            DataResult<Tag> encodedBlockState = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, event.getState());
            encodedBlockState.result().ifPresent(tag -> {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO block (actor, world, x, y, z, blockType, blockState, action, time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
                    preparedStatement.setString(1, event.getPlayer().getScoreboardName());
                    preparedStatement.setInt(2, Cache.DIMENSIONS.get(event.getPlayer().level().dimension().location()));
                    preparedStatement.setInt(3, event.getPos().getX());
                    preparedStatement.setInt(4, event.getPos().getY());
                    preparedStatement.setInt(5, event.getPos().getZ());
                    preparedStatement.setString(6, ForgeRegistries.BLOCKS.getKey(event.getState().getBlock()).toString());
                    preparedStatement.setString(7, tag.toString());
                    preparedStatement.setInt(8, BlockAction.BREAK.ordinal());
                    preparedStatement.setLong(9, System.currentTimeMillis());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    LOGGER.debug("Block break event logged");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
