package plus.dragons.pipeslide;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PipeSlide.MOD_ID)
public class PipeSlide
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MOD_ID = "pipeslide";

    public PipeSlide() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
    }

    public static ResourceLocation genRL(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
