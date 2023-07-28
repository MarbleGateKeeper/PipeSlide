package plus.dragons.pipeslide.entry;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.pipeslide.PipeSlide;
import plus.dragons.pipeslide.content.pipes.specific.base.doubleconnect.PipeDoubleConnectRenderer;
import plus.dragons.pipeslide.content.pipes.specific.node.PipeNodeBlockEntity;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PipeSlide.MOD_ID);
    public static final RegistryObject<BlockEntityType<PipeNodeBlockEntity>> PIPE_NODE = BLOCK_ENTITIES.register("pipe_node",
            () -> BlockEntityType.Builder.of(PipeNodeBlockEntity::new, ModBlocks.PIPE_NODE.get()).build(null));
    public static final RegistryObject<BlockEntityType<PipePlatformBlockEntity>> PIPE_PLATFORM = BLOCK_ENTITIES.register("pipe_platform",
            () -> BlockEntityType.Builder.of(PipePlatformBlockEntity::new, ModBlocks.PIPE_PLATFORM.get()).build(null));

    public static void registerRenderer(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntities.PIPE_NODE.get(), PipeDoubleConnectRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.PIPE_PLATFORM.get(), PipeDoubleConnectRenderer::new);
        });
    }
}
