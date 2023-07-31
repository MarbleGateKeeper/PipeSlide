package plus.dragons.pipeslide.content.pipes.specific.platform.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformBlockEntity;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeMobPlatformBlockEntity extends PipePlatformBlockEntity<Mob> {

    public PipeMobPlatformBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_MOB_PLATFORM.get(), pos, state);
    }

    @Override
    protected Class<Mob> getPassengerClass() {
        return Mob.class;
    }
}
