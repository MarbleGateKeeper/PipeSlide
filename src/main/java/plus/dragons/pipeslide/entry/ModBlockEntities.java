package plus.dragons.pipeslide.entry;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.pipeslide.PipeSlide;
import plus.dragons.pipeslide.content.pipes.specific.standard.PipeNodeBlockEntity;
import plus.dragons.pipeslide.content.pipes.specific.standard.PipeNodeRenderer;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOKC_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PipeSlide.MOD_ID);
    public static final RegistryObject<BlockEntityType<PipeNodeBlockEntity>> PIPE_NODE = BLOKC_ENTITIES.register("pipe_node", () -> BlockEntityType.Builder.of(PipeNodeBlockEntity::new, ModBlocks.PIPE_NODE.get()).build(null));

    public static void registerRenderer(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntities.PIPE_NODE.get(), PipeNodeRenderer::new);
        });
    }
}
