package dk.magnusjensen.squareprotect.listener;

import dk.magnusjensen.squareprotect.commands.SquareProtectCommand;
import dk.magnusjensen.squareprotect.listener.block.BlockBreakListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventListenerRegistrator {
    public static void registerEventListeners() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;



        // Block Events
        forgeBus.addListener(BlockBreakListener::onBlockBreak);

        // Events needed for mod setup
        forgeBus.addListener(EventListenerRegistrator::registerCommands);
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(SquareProtectCommand.register());
    }
}
