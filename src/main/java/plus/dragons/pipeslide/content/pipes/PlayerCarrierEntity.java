package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import plus.dragons.pipeslide.entry.ModEntityTypes;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

public class PlayerCarrierEntity extends Entity {

    private INavigationPipeBE navigator = null;
    private BlockPos nextNode = null;
    private float currentT;
    private float currentSpeed;
    private boolean done;
    private int lSteps;
    private double lx;
    private double ly;
    private double lz;
    private double lxd;
    private double lyd;
    private double lzd;

    public PlayerCarrierEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PlayerCarrierEntity(Level pLevel,INavigationPipeBE navigator, BlockPos nextNode) {
        super(ModEntityTypes.CARRIER.get(), pLevel);
        this.navigator = navigator;
        this.nextNode = nextNode;
        this.currentT = 0;
        //this.currentSpeed = 0;
        this.currentSpeed = 8F / 20;
    }


    @Override
    public void tick() {
        if (this.level.isClientSide) {
            if (this.lSteps > 0) {
                double d5 = this.getX() + (this.lx - this.getX()) / (double)this.lSteps;
                double d6 = this.getY() + (this.ly - this.getY()) / (double)this.lSteps;
                double d7 = this.getZ() + (this.lz - this.getZ()) / (double)this.lSteps;
                --this.lSteps;
                this.setPos(d5, d6, d7);
            } else {
                this.reapplyPosition();
            }
            reapplyPosition();
        } else {
            if (getFirstPassenger() == null){
                this.remove(RemovalReason.DISCARDED);
                return;
            }
            if(this.navigator!=null){
                moveAlongPipe();
            }
        }
    }

    public void moveAlongPipe(){
        var result = navigator.navigate(this,nextNode,currentSpeed,currentT);
        this.navigator = result.navigatorNext();
        this.nextNode = result.nextNode();
        this.currentT = result.t();
        this.currentSpeed = result.speed();

    }

    protected double getMaxSpeed() {
        return (this.isInWater() ? 7.5 : 10.0D) / 20.0D;
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
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {}

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.lx = pX;
        this.ly = pY;
        this.lz = pZ;
        this.lSteps = pPosRotationIncrements + 2;
        this.setDeltaMovement(this.lxd, this.lyd, this.lzd);
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @Override
    public void lerpMotion(double pX, double pY, double pZ) {
        this.lxd = pX;
        this.lyd = pY;
        this.lzd = pZ;
        this.setDeltaMovement(this.lxd, this.lyd, this.lzd);
    }

}
