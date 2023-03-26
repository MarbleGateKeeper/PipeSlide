package plus.dragons.pipeslide.entry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.pipeslide.PipeSlide;
import plus.dragons.pipeslide.content.pipes.specific.standard.PipeNodeNodeBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PipeSlide.MOD_ID);

    // PIPE
    public static final RegistryObject<Block> PIPE_NODE = BLOCKS.register("pipe_node", PipeNodeNodeBlock::new);
    public static final RegistryObject<Block> PIPE_CURVE_ANCHOR = BLOCKS.register("pipe_curve_anchor", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(128.0f).noOcclusion()));
}
