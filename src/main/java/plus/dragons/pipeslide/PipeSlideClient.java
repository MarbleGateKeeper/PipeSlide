package plus.dragons.pipeslide;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.entry.ModEntityTypes;
import plus.dragons.pipeslide.entry.ModItems;
import plus.dragons.pipeslide.foundation.client.model.AllBlockPartials;
import plus.dragons.pipeslide.foundation.client.model.PartialModel;

public class PipeSlideClient {
    public PipeSlideClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModBlockEntities::registerRenderer);
        modEventBus.addListener(ModItems::registerCreativeTab);
        modEventBus.addListener(PipeSlideClient::clientInit);
        modEventBus.addListener(PartialModel::onModelRegistry);
        modEventBus.addListener(PartialModel::onModelBake);
        modEventBus.addListener(ModEntityTypes::registerRenderer);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        AllBlockPartials.init();
    }
}
