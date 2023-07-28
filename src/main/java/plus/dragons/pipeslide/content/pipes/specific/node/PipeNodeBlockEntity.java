package plus.dragons.pipeslide.content.pipes.specific.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.specific.base.doubleconnect.PipeDoubleConnectBlockEntity;
import plus.dragons.pipeslide.entry.ModBlockEntities;
public class PipeNodeBlockEntity extends PipeDoubleConnectBlockEntity {

    public PipeNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_NODE.get(), pos, state);
    }
}
