package plus.dragons.pipeslide;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import plus.dragons.pipeslide.entry.*;

// TODO fix still some Y-fighting on pipe rendering
// TODO add a tool to configure texture
// TODO add crafting recipes
@Mod(PipeSlide.MOD_ID)
public class PipeSlide {

    public static final String MOD_ID = "pipeslide";

    public PipeSlide() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModCreativeModeTab.TABS.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> PipeSlideClient::new);
    }

    public static ResourceLocation genRL(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
