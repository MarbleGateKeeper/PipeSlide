package plus.dragons.pipeslide.content.pipes.specific.platform.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeItemReceiverBlock extends PipeDoubleConnectBlock<PipeItemReceiverBlockEntity> {
    public PipeItemReceiverBlock() {
        super();
    }

    @Override
    public Class<PipeItemReceiverBlockEntity> getTileEntityClass() {
        return PipeItemReceiverBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipeItemReceiverBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_ITEM_RECEIVER.get();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }
}
