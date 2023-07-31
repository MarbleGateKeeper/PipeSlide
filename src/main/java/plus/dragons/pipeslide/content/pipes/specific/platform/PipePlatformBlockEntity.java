package plus.dragons.pipeslide.content.pipes.specific.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.content.pipes.CarrierEntity;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlockEntity;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

public abstract class PipePlatformBlockEntity<T extends Entity> extends PipeDoubleConnectBlockEntity {

    public PipePlatformBlockEntity(BlockEntityType<? extends PipePlatformBlockEntity> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
        setLazyTickRate(10);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!level.isClientSide && (connectionA != null || connectionB != null)) {
            level.getEntitiesOfClass(getPassengerClass(), new AABB(getBlockPos()).expandTowards(0, 0.5, 0),
                    passenger -> !passenger.isSpectator() & passenger.getVehicle() == null).forEach(this::startPipeRide);
        }
    }

    abstract protected Class<? extends T> getPassengerClass();

    private void startPipeRide(T passenger) {
        BlockPos next;
        if (connectionA == null) {
            next = connectionB.to;
        } else {
            if (connectionB == null) {
                next = connectionA.to;
            } else {
                Vec3 lookVec = passenger.getLookAngle();
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
        passenger.startRiding(carrier, true);
    }
}
