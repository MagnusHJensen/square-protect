package dk.magnusjensen.squareprotect.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dk.magnusjensen.squareprotect.SquareProtect;
import dk.magnusjensen.squareprotect.db.Database;
import dk.magnusjensen.squareprotect.db.lookup.BlockLookup;
import dk.magnusjensen.squareprotect.db.lookup.SessionLookup;
import dk.magnusjensen.squareprotect.utils.Cache;
import dk.magnusjensen.squareprotect.utils.ChatComponent;
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
import java.util.Collections;
import java.util.List;

public class LookupSubCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("lookup")
            .executes(ctx -> lookupEvents(ctx.getSource(), ctx.getSource().getPlayerOrException()));
    }

    private static int lookupEvents(CommandSourceStack source, ServerPlayer player) {
        try {
            List<ChatComponent> comps = BlockLookup.lookup(Cache.DIMENSIONS.get(player.level().dimension().location()));
            comps.addAll(SessionLookup.lookup(Cache.DIMENSIONS.get(player.level().dimension().location())));
            Collections.sort(comps);

            int limit = 10;
            int page = 0;
            int total = comps.size();
            int totalPages = (int) Math.ceil((float) total / limit);

            MutableComponent mainComp = Component.literal("----- SquareProtect Lookup Results -----\n");
            for (int i = page * limit; i < (page + 1) * limit; i++) {
                if (i >= comps.size()) {
                    break;
                }
                ChatComponent comp = comps.get(i);
                mainComp.append(comp.chatComponent());
            }

            mainComp.append("Page " + (page + 1) + "/" + totalPages);
            source.sendSuccess(() -> mainComp, false);
            return 0;
        } catch (SQLException e) {
            SquareProtect.LOGGER.error("Failed to lookup events", e);
            return -1;
        }
    }
}
