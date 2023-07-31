package plus.dragons.pipeslide.content.pipes.specific.platform.minecart;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeMinecartStationBlock extends PipeDoubleConnectBlock<PipeMinecartStationBlockEntity> {
    public PipeMinecartStationBlock() {
        super();
    }

    @Override
    public Class<PipeMinecartStationBlockEntity> getTileEntityClass() {
        return PipeMinecartStationBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipeMinecartStationBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_MINECART_STATION.get();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }
}
