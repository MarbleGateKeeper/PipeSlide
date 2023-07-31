package plus.dragons.pipeslide.content.pipes.specific.platform.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformBlockEntity;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipePlayerPlatformBlockEntity extends PipePlatformBlockEntity<Player> {

    public PipePlayerPlatformBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_PLAYER_PLATFORM.get(), pos, state);
    }

    @Override
    protected Class<Player> getPassengerClass() {
        return Player.class;
    }
}
