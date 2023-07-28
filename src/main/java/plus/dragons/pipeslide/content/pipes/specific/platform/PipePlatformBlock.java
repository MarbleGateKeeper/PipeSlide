package plus.dragons.pipeslide.content.pipes.specific.platform;

import net.minecraft.world.level.block.entity.BlockEntityType;
import plus.dragons.pipeslide.content.pipes.specific.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipePlatformBlock extends PipeDoubleConnectBlock<PipePlatformBlockEntity> {
    public PipePlatformBlock() {
        super();
    }

    @Override
    public Class<PipePlatformBlockEntity> getTileEntityClass() {
        return PipePlatformBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipePlatformBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_PLATFORM.get();
    }
}
