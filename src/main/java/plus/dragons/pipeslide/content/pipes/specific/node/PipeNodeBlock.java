package plus.dragons.pipeslide.content.pipes.specific.node;

import net.minecraft.world.level.block.entity.BlockEntityType;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeNodeBlock extends PipeDoubleConnectBlock<PipeNodeBlockEntity> {
    public PipeNodeBlock() {
        super();
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
