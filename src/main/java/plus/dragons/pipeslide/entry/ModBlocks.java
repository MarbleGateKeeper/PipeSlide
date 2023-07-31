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
import plus.dragons.pipeslide.content.pipes.specific.platform.boat.PipeBoatDockBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.item.PipeItemReceiverBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.item.PipeItemReceiverBlockEntity;
import plus.dragons.pipeslide.content.pipes.specific.platform.minecart.PipeMinecartStationBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.mob.PipeMobPlatformBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.player.PipePlayerPlatformBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PipeSlide.MOD_ID);

    // PIPE
    public static final RegistryObject<PipeNodeBlock> PIPE_NODE = BLOCKS.register("pipe_node", PipeNodeBlock::new);
    public static final RegistryObject<PipePlayerPlatformBlock> PIPE_PLAYER_PLATFORM = BLOCKS.register("pipe_player_platform", PipePlayerPlatformBlock::new);
    public static final RegistryObject<PipeMobPlatformBlock> PIPE_MOB_PLATFORM = BLOCKS.register("pipe_mob_platform", PipeMobPlatformBlock::new);
    public static final RegistryObject<PipeMinecartStationBlock> PIPE_MINECART_STATION = BLOCKS.register("pipe_minecart_station", PipeMinecartStationBlock::new);
    public static final RegistryObject<PipeBoatDockBlock> PIPE_BOAT_DOCK = BLOCKS.register("pipe_boat_dock", PipeBoatDockBlock::new);
    public static final RegistryObject<PipeItemReceiverBlock> PIPE_ITEM_RECEIVER = BLOCKS.register("pipe_item_receiver", PipeItemReceiverBlock::new);
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
}
