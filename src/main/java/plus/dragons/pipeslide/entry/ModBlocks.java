package plus.dragons.pipeslide.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import plus.dragons.pipeslide.PipeSlide;
import plus.dragons.pipeslide.content.pipes.specific.node.PipeNodeBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PipeSlide.MOD_ID);

    // PIPE
    public static final RegistryObject<PipeNodeBlock> PIPE_NODE = BLOCKS.register("pipe_node", PipeNodeBlock::new);
    public static final RegistryObject<PipePlatformBlock> PIPE_PLATFORM = BLOCKS.register("pipe_platform", PipePlatformBlock::new);
    public static final RegistryObject<Block> PIPE_CURVE_ANCHOR = BLOCKS.register("pipe_curve_anchor", () ->
            new Block(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .strength(128.0f)
                    .isSuffocating(($1,$2,$3)->false)
                    .isViewBlocking(($1,$2,$3)->false)) {
                @Override
                public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                    return 5;
                }

                @Override
                public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pReader, @NotNull BlockPos pPos) {
                    return true;
                }

                @Override
                public float getShadeBrightness(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
                    return 1.0F;
                }
    });

    private static boolean never(BlockState blockState, BlockGetter level, BlockPos pos) {
        return false;
    }
}
