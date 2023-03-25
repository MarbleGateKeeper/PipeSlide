package plus.dragons.pipeslide;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.entry.ModItems;

public class PipeSlideClient {
    public PipeSlideClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModBlockEntities::registerRenderer);
        modEventBus.addListener(ModItems::registerCreativeTab);
    }
}
