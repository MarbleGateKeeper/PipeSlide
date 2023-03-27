package plus.dragons.pipeslide.content.pipes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import plus.dragons.pipeslide.entry.ModEntityTypes;

public class PlayerCarrierEntity extends Entity {

    public PlayerCarrierEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PlayerCarrierEntity(Level pLevel) {
        super(ModEntityTypes.CARRIER.get(), pLevel);
    }


    @Override
    public void tick() {
        if (this.level.isClientSide) {
            // TODO
        } else {
            if (getPassengers().isEmpty()){
                this.remove(RemovalReason.DISCARDED);
            }
            moveAlongPipe();
        }
    }

    public void moveAlongPipe(){

    }

    protected double getMaxSpeed() {
        return (this.isInWater() ? 6.0D : 10.0D) / 20.0D;
    }

    @Override
    public boolean canCollideWith(@NotNull Entity pEntity) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
