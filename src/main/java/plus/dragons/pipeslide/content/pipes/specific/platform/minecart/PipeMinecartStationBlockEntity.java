package plus.dragons.pipeslide.content.pipes.specific.platform.minecart;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformBlockEntity;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeMinecartStationBlockEntity extends PipePlatformBlockEntity<AbstractMinecart> {

    public PipeMinecartStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_MINECART_STATION.get(), pos, state);
    }

    @Override
    protected Class<AbstractMinecart> getPassengerClass() {
        return AbstractMinecart.class;
    }
}
