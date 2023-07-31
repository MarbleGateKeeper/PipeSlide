package plus.dragons.pipeslide.content.pipes.specific.platform.boat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeBoatDockBlock extends PipeDoubleConnectBlock<PipeBoatDockBlockEntity> {
    public PipeBoatDockBlock() {
        super();
    }

    @Override
    public Class<PipeBoatDockBlockEntity> getTileEntityClass() {
        return PipeBoatDockBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipeBoatDockBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_BOAT_DOCK.get();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }
}
