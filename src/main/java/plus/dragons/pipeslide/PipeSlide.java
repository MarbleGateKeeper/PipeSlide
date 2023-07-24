package plus.dragons.pipeslide;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.entry.ModBlocks;
import plus.dragons.pipeslide.entry.ModEntityTypes;
import plus.dragons.pipeslide.entry.ModItems;

import java.awt.*;

@Mod(PipeSlide.MOD_ID)
public class PipeSlide
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MOD_ID = "pipeslide";

    public PipeSlide() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOKC_ENTITIES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> PipeSlideClient::new);
    }

    public static ResourceLocation genRL(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
