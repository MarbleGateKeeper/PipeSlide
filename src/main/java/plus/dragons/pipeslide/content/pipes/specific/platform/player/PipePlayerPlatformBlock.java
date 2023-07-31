package plus.dragons.pipeslide.content.pipes.specific.platform.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }
}
