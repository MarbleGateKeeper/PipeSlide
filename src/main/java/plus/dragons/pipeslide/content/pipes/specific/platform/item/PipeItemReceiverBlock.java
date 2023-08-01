package plus.dragons.pipeslide.content.pipes.specific.platform.item;

import net.minecraft.world.level.block.entity.BlockEntityType;
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
}
