package dk.magnusjensen.squareprotect;

import com.mojang.logging.LogUtils;
import dk.magnusjensen.squareprotect.listener.EventListenerRegistrator;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SquareProtect.MODID)
public class SquareProtect
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "squareprotect";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    public SquareProtect()
    {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);

        EventListenerRegistrator.registerEventListeners();


    }



}
