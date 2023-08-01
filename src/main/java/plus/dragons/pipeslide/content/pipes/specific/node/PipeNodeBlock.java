package plus.dragons.pipeslide.content.pipes.specific.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeNodeBlock extends PipeDoubleConnectBlock<PipeNodeBlockEntity> {
    public PipeNodeBlock() {
        super();
    }
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }

    @Override
    public Class<PipeNodeBlockEntity> getTileEntityClass() {
        return PipeNodeBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipeNodeBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_NODE.get();
    }
}
