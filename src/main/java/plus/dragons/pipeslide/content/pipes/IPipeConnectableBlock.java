package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.foundation.block.IEntityBlock;

public interface IPipeConnectableBlock<T extends BlockEntity> extends IEntityBlock<T> {
    boolean hasConnectableEnd(BlockGetter world, BlockPos pos);

    boolean canConnectTo(BlockGetter world, BlockPos pos, BlockPos connectTarget);

    void addPipeConnection(BlockGetter world, BlockPos pos, BlockPos connectTarget, Direction playerFacing, boolean primaryForRender, @Nullable BlockPos midPoint);

    void removePipeConnection(BlockGetter world, BlockPos pos, BlockPos removeTarget);
}
