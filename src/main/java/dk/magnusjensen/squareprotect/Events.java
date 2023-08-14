package dk.magnusjensen.squareprotect;

import com.mojang.logging.LogUtils;
import dk.magnusjensen.squareprotect.db.Database;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = SquareProtect.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        Database.createDatabaseTables();
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[SquareProtect] Server started");
    }
}
