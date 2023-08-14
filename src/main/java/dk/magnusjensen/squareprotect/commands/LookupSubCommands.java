package dk.magnusjensen.squareprotect.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dk.magnusjensen.squareprotect.db.Database;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.TextComponentHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class LookupSubCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("lookup")
            .executes(ctx -> lookupEvents(ctx.getSource(), ctx.getSource().getPlayerOrException()));
    }

    private static int lookupEvents(CommandSourceStack source, ServerPlayer player) {
        BlockPos pos = player.blockPosition();
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            int pageSize = 10;
            int page = 1;
            ResultSet set = statement.executeQuery("SELECT *, COUNT() OVER() as totalCount FROM block WHERE " + "x BETWEEN " + (pos.getX() - 10) + " AND " + (pos.getX() + 10) + " AND y BETWEEN " + (pos.getY() - 10) + " AND " + (pos.getY() + 10) + " AND z BETWEEN " + (pos.getZ() - 10) + " AND " + (pos.getZ() + 10) +  " ORDER BY time DESC LIMIT " + pageSize + " OFFSET " + (page - 1 * pageSize) + ";");
            MutableComponent comp = Component.literal("Found " + set.getInt("totalCount") + " events\n");

            while (set.next()) {
                comp.append(Component.literal(set.getString("time") + " "));
                comp.append(Component.literal(set.getString("username") + " "));
                comp.append(Component.literal(set.getString("action") + " "));
                comp.append(Component.literal(set.getString("x") + " "));
                comp.append(Component.literal(set.getString("y") + " "));
                comp.append(Component.literal(set.getString("z") + "\n"));
            }
            comp.append(Component.literal("<< " + page + " of " + (set.getInt("totalCount") / pageSize + 1) + " >>"));
            source.sendSuccess(() -> comp, false);
            set.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
