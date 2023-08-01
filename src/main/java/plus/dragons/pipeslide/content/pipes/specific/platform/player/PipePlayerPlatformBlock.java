package plus.dragons.pipeslide.content.pipes.specific.platform.player;

import net.minecraft.world.level.block.entity.BlockEntityType;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipePlayerPlatformBlock extends PipeDoubleConnectBlock<PipePlayerPlatformBlockEntity> {
    public PipePlayerPlatformBlock() {
        super();
    }

    @Override
    public Class<PipePlayerPlatformBlockEntity> getTileEntityClass() {
        return PipePlayerPlatformBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipePlayerPlatformBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_PLAYER_PLATFORM.get();
    }
}
