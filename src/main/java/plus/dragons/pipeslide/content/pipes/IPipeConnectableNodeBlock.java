package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.Nullable;

public interface IPipeConnectableNodeBlock {
    boolean hasConnectableEnd(BlockGetter world, BlockPos pos);

    boolean canConnectTo(BlockGetter world, BlockPos pos, BlockPos connectTarget);

    void addPipeConnection(BlockGetter world, BlockPos pos, BlockPos connectTarget, Direction playerFacing, boolean primaryForRender, @Nullable BlockPos midPoint);
}
