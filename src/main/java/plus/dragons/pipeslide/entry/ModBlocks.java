package plus.dragons.pipeslide.entry;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.pipeslide.PipeSlide;
import plus.dragons.pipeslide.content.pipes.specific.node.PipeNodeBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.boat.PipeBoatDockBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.item.PipeItemReceiverBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.minecart.PipeMinecartStationBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.mob.PipeMobPlatformBlock;
import plus.dragons.pipeslide.content.pipes.specific.platform.player.PipePlayerPlatformBlock;
import plus.dragons.pipeslide.content.utility.PipeCurveAnchorBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PipeSlide.MOD_ID);
    // PIPE
    public static final RegistryObject<PipeNodeBlock> PIPE_NODE = BLOCKS.register("pipe_node", PipeNodeBlock::new);
    public static final RegistryObject<PipePlayerPlatformBlock> PIPE_PLAYER_PLATFORM = BLOCKS.register("pipe_player_platform", PipePlayerPlatformBlock::new);
    public static final RegistryObject<PipeMobPlatformBlock> PIPE_MOB_PLATFORM = BLOCKS.register("pipe_mob_platform", PipeMobPlatformBlock::new);
    public static final RegistryObject<PipeMinecartStationBlock> PIPE_MINECART_STATION = BLOCKS.register("pipe_minecart_station", PipeMinecartStationBlock::new);
    public static final RegistryObject<PipeBoatDockBlock> PIPE_BOAT_DOCK = BLOCKS.register("pipe_boat_dock", PipeBoatDockBlock::new);
    public static final RegistryObject<PipeItemReceiverBlock> PIPE_ITEM_RECEIVER = BLOCKS.register("pipe_item_receiver", PipeItemReceiverBlock::new);
    public static final RegistryObject<PipeCurveAnchorBlock> PIPE_CURVE_ANCHOR = BLOCKS.register("pipe_curve_anchor", PipeCurveAnchorBlock::new);
}
