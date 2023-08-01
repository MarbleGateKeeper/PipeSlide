package plus.dragons.pipeslide;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import plus.dragons.pipeslide.content.pipes.CarrierEntity;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectRenderer;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformWithIndicatorRenderer;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.entry.ModEntityTypes;
import plus.dragons.pipeslide.foundation.client.renderer.BlandEntityRenderer;

public class PipeSlideClient {
    public PipeSlideClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(PipeSlideClient::registerBlockEntityRenderer);
        modEventBus.addListener(PipeSlideClient::registerEntityRenderer);
        MinecraftForge.EVENT_BUS.addListener(CarrierEntity::modifyFOV);
    }

    public static void registerBlockEntityRenderer(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntities.PIPE_NODE.get(), PipeDoubleConnectRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.PIPE_PLAYER_PLATFORM.get(),
                    (context) -> new PipePlatformWithIndicatorRenderer<>(context, () -> Blocks.PLAYER_HEAD.asItem().getDefaultInstance()));
            BlockEntityRenderers.register(ModBlockEntities.PIPE_MOB_PLATFORM.get(),
                    (context) -> new PipePlatformWithIndicatorRenderer<>(context, () -> Blocks.PIGLIN_HEAD.asItem().getDefaultInstance()));
            BlockEntityRenderers.register(ModBlockEntities.PIPE_MINECART_STATION.get(),
                    (context) -> new PipePlatformWithIndicatorRenderer<>(context, Items.MINECART::getDefaultInstance));
            BlockEntityRenderers.register(ModBlockEntities.PIPE_BOAT_DOCK.get(),
                    (context) -> new PipePlatformWithIndicatorRenderer<>(context, Items.OAK_BOAT::getDefaultInstance));
            BlockEntityRenderers.register(ModBlockEntities.PIPE_ITEM_RECEIVER.get(), (
                    context) -> new PipePlatformWithIndicatorRenderer<>(context, Items.DIAMOND::getDefaultInstance));
        });
    }

    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.CARRIER.get(), BlandEntityRenderer::new);
    }

}
