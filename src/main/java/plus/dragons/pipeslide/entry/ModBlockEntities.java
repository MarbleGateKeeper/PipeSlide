package plus.dragons.pipeslide.entry;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.pipeslide.PipeSlide;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectRenderer;
import plus.dragons.pipeslide.content.pipes.specific.node.PipeNodeBlockEntity;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformWithIndicatorRenderer;
import plus.dragons.pipeslide.content.pipes.specific.platform.boat.PipeBoatDockBlockEntity;
import plus.dragons.pipeslide.content.pipes.specific.platform.item.PipeItemReceiverBlockEntity;
import plus.dragons.pipeslide.content.pipes.specific.platform.minecart.PipeMinecartStationBlockEntity;
import plus.dragons.pipeslide.content.pipes.specific.platform.mob.PipeMobPlatformBlockEntity;
import plus.dragons.pipeslide.content.pipes.specific.platform.player.PipePlayerPlatformBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PipeSlide.MOD_ID);
    public static final RegistryObject<BlockEntityType<PipeNodeBlockEntity>> PIPE_NODE = BLOCK_ENTITIES.register("pipe_node",
            () -> BlockEntityType.Builder.of(PipeNodeBlockEntity::new, ModBlocks.PIPE_NODE.get()).build(null));
    public static final RegistryObject<BlockEntityType<PipePlayerPlatformBlockEntity>> PIPE_PLAYER_PLATFORM = BLOCK_ENTITIES.register("pipe_player_platform",
            () -> BlockEntityType.Builder.of(PipePlayerPlatformBlockEntity::new, ModBlocks.PIPE_PLAYER_PLATFORM.get()).build(null));
    public static final RegistryObject<BlockEntityType<PipeMobPlatformBlockEntity>> PIPE_MOB_PLATFORM = BLOCK_ENTITIES.register("pipe_mob_platform",
            () -> BlockEntityType.Builder.of(PipeMobPlatformBlockEntity::new, ModBlocks.PIPE_MOB_PLATFORM.get()).build(null));
    public static final RegistryObject<BlockEntityType<PipeMinecartStationBlockEntity>> PIPE_MINECART_STATION = BLOCK_ENTITIES.register("pipe_minecart_station",
            () -> BlockEntityType.Builder.of(PipeMinecartStationBlockEntity::new, ModBlocks.PIPE_MINECART_STATION.get()).build(null));
    public static final RegistryObject<BlockEntityType<PipeBoatDockBlockEntity>> PIPE_BOAT_DOCK = BLOCK_ENTITIES.register("pipe_boat_dock",
            () -> BlockEntityType.Builder.of(PipeBoatDockBlockEntity::new, ModBlocks.PIPE_BOAT_DOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<PipeItemReceiverBlockEntity>> PIPE_ITEM_RECEIVER = BLOCK_ENTITIES.register("pipe_item_receiver",
            () -> BlockEntityType.Builder.of(PipeItemReceiverBlockEntity::new, ModBlocks.PIPE_ITEM_RECEIVER.get()).build(null));

    public static void registerRenderer(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntities.PIPE_NODE.get(), PipeDoubleConnectRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.PIPE_PLAYER_PLATFORM.get(),
                    (context)-> new PipePlatformWithIndicatorRenderer<>(context, ()-> Blocks.PLAYER_HEAD.asItem().getDefaultInstance()));
            BlockEntityRenderers.register(ModBlockEntities.PIPE_MOB_PLATFORM.get(),
                    (context)-> new PipePlatformWithIndicatorRenderer<>(context, ()-> Blocks.PIGLIN_HEAD.asItem().getDefaultInstance()));
            BlockEntityRenderers.register(ModBlockEntities.PIPE_MINECART_STATION.get(),
                    (context)-> new PipePlatformWithIndicatorRenderer<>(context, Items.MINECART::getDefaultInstance));
            BlockEntityRenderers.register(ModBlockEntities.PIPE_BOAT_DOCK.get(),
                    (context)-> new PipePlatformWithIndicatorRenderer<>(context, Items.OAK_BOAT::getDefaultInstance));
            BlockEntityRenderers.register(ModBlockEntities.PIPE_ITEM_RECEIVER.get(), (
                    context)-> new PipePlatformWithIndicatorRenderer<>(context, Items.DIAMOND::getDefaultInstance));
        });
    }
}
