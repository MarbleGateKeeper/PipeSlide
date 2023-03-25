package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IPipeConnectableNodeBlock {
    boolean hasConnectableEnd(BlockGetter world, BlockPos pos, BlockState state);

    void addPipeConnection(BlockGetter world, BlockPos pos, BlockState state, BlockPos connectTarget, boolean primaryForRender, @Nullable BlockPos midPoint);
}
