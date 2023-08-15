package dk.magnusjensen.squareprotect.db.lookup;

import dk.magnusjensen.squareprotect.db.Database;
import dk.magnusjensen.squareprotect.db.actions.BlockAction;
import dk.magnusjensen.squareprotect.db.actions.SessionAction;
import dk.magnusjensen.squareprotect.utils.ChatComponent;
import dk.magnusjensen.squareprotect.utils.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionLookup {
    public static List<ChatComponent> lookup(int world) throws SQLException {
        List<ChatComponent> list = new ArrayList<>();
        try(Connection connection = Database.getConnection()) {
            ResultSet set = connection.createStatement().executeQuery("SELECT u.username, s.x, s.y, s.z, s.action, s.time FROM session s INNER JOIN user u on u.uuid = s.uuid WHERE s.world = " + world + " ORDER BY u.time;");
            connection.commit();
            while (set.next()) {
                String actor = set.getString("username");
                SessionAction action = SessionAction.values()[set.getInt("action")];
                int x = set.getInt("x");
                int y = set.getInt("y");
                int z = set.getInt("z");
                long timestamp = set.getLong("time");
                Component chatComp = ChatUtils.getTimeSince(timestamp).withStyle(style -> style.withColor(ChatFormatting.GRAY))
                    .append(Component.literal(" - ").withStyle(style -> Style.EMPTY.withColor(ChatFormatting.WHITE)))
                    .append(Component.literal(actor).withStyle(ChatFormatting.DARK_AQUA))
                    .append(Component.literal(" " + action + "\n").withStyle(ChatFormatting.WHITE))
                    .append("        ").append(Component.literal(String.format("^ (x%d/y%d/z%d/%s)\n", x, y, z, world)).withStyle(ChatFormatting.DARK_GRAY));
                list.add(new ChatComponent(chatComp, timestamp));
            }
            set.close();
        }
        return list;
    }
}
