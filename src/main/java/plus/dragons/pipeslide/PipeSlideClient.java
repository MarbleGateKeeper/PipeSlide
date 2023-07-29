package plus.dragons.pipeslide;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import plus.dragons.pipeslide.content.pipes.CarrierEntity;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.entry.ModEntityTypes;

public class PipeSlideClient {
    public PipeSlideClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModBlockEntities::registerRenderer);
        modEventBus.addListener(ModEntityTypes::registerRenderer);
        MinecraftForge.EVENT_BUS.addListener(CarrierEntity::modifyFOV);
    }

}
