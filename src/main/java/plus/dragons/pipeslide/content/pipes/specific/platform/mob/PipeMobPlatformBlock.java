package plus.dragons.pipeslide.content.pipes.specific.platform.mob;

import net.minecraft.world.level.block.entity.BlockEntityType;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeMobPlatformBlock extends PipeDoubleConnectBlock<PipeMobPlatformBlockEntity> {
    public PipeMobPlatformBlock() {
        super();
    }

    @Override
    public Class<PipeMobPlatformBlockEntity> getTileEntityClass() {
        return PipeMobPlatformBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipeMobPlatformBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_MOB_PLATFORM.get();
    }
}
