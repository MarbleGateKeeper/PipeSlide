package plus.dragons.pipeslide.content.pipes.specific.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.content.pipes.CarrierEntity;
import plus.dragons.pipeslide.content.pipes.specific.base.doubleconnect.PipeDoubleConnectBlockEntity;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

public class PipePlatformBlockEntity extends PipeDoubleConnectBlockEntity {

    public PipePlatformBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_PLATFORM.get(), pos, state);
        setLazyTickRate(10);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!level.isClientSide && (connectionA != null || connectionB != null)) {
            level.getEntitiesOfClass(Player.class, new AABB(getBlockPos()).expandTowards(0, 1, 0),
                    player -> !player.isSpectator() & player.getVehicle() == null).forEach(this::startPipeRide);
        }
    }

    private void startPipeRide(Player player) {
        BlockPos next;
        if (connectionA == null) {
            next = connectionB.to;
        } else {
            if (connectionB == null) {
                next = connectionA.to;
            } else {
                Vec3 lookVec = player.getLookAngle();
                Vec3 aVec = VecHelper.getCenterOf(this.getBlockPos()).vectorTo(VecHelper.getCenterOf(connectionA.to));
                Vec3 bVec = VecHelper.getCenterOf(this.getBlockPos()).vectorTo(VecHelper.getCenterOf(connectionB.to));
                if (aVec.dot(lookVec) > bVec.dot(lookVec)) {
                    next = connectionA.to;
                } else next = connectionB.to;
            }
        }
        var carrier = new CarrierEntity(level, this, next);
        carrier.setPos(VecHelper.getCenterOf(getBlockPos()));
        level.addFreshEntity(carrier);
        player.startRiding(carrier, true);
    }
}
