package plus.dragons.pipeslide.content.pipes.specific.platform.boat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformBlockEntity;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeBoatDockBlockEntity extends PipePlatformBlockEntity<Boat> {

    public PipeBoatDockBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_BOAT_DOCK.get(), pos, state);
    }

    @Override
    protected Class<Boat> getPassengerClass() {
        return Boat.class;
    }
}
