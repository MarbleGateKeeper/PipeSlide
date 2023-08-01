package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import plus.dragons.pipeslide.entry.ModEntityTypes;

public class CarrierEntity extends Entity {
    private static final EntityDataAccessor<Float> CURRENT_SPEED = SynchedEntityData.defineId(CarrierEntity.class, EntityDataSerializers.FLOAT);
    private INavigationPipeBE navigator = null;
    private BlockPos nextNode = null;
    private float currentT;
    private float currentSpeed;
    private int lSteps;
    private double lx;
    private double ly;
    private double lz;
    private int beforeEjectTick = 5;
    private boolean readyToEject = false;

    public CarrierEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CarrierEntity(Level pLevel, INavigationPipeBE navigator, BlockPos nextNode) {
        super(ModEntityTypes.CARRIER.get(), pLevel);
        this.navigator = navigator;
        this.nextNode = nextNode;
        this.currentT = 0;
        this.currentSpeed = getInitialSpeed();
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(CURRENT_SPEED, getInitialSpeed());
    }


    private static float getInitialSpeed(){
        return 0.15F ;
    }

    public float getStandardSpeed() {
        return 0.5F;
    }

    public float getSyncCurrentSpeed() {
        return getEntityData().get(CURRENT_SPEED);
    }

    @Override
    public boolean isPassenger() {
        return false;
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            if (this.lSteps > 0) {
                double d0 = this.getX() + (this.lx - this.getX()) / (double)this.lSteps;
                double d1 = this.getY() + (this.ly - this.getY()) / (double)this.lSteps;
                double d2 = this.getZ() + (this.lz - this.getZ()) / (double)this.lSteps;
                --this.lSteps;
                this.setPos(d0, d1, d2);
            }
            reapplyPosition();
            adjustRotation();
            if(getFirstPassenger()!=null){
                positionRider(getFirstPassenger());
                adjustPassengerRotation();
            }
        } else {
            if (getFirstPassenger() == null) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }
            if (this.navigator != null) {
                moveAlongPipe();
                markHurt();
            }
            else if (readyToEject){
                if(beforeEjectTick > 0){
                    move(MoverType.SELF,getDeltaMovement());
                    beforeEjectTick--;
                } else {
                    ejectPassengers();
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
    }

    private void moveAlongPipe() {
        var result = navigator.navigate(this, nextNode, currentSpeed, currentT);
        if(result.speed()!=getSyncCurrentSpeed()){
            getEntityData().set(CURRENT_SPEED,result.speed());
        }
        navigator = result.navigatorNext();
        if(navigator==null){
            readyToEject = true;
            if(getFirstPassenger() instanceof Player)
                beforeEjectTick = 10;
            return;
        }
        nextNode = result.nextNode();
        currentT = result.t();
        currentSpeed = result.speed();
    }

    private void adjustRotation(){
        double d1 = this.xo - this.getX();
        double d3 = this.zo - this.getZ();
        this.setYRot((float)(Mth.atan2(d3, d1) * 180.0D / Math.PI));
        double d4 = Mth.wrapDegrees(this.getYRot() - this.yRotO);
        if (d4 < -170.0D || d4 >= 170.0D) {
            this.setYRot(this.getYRot() + 180.0F);
        }
    }

    private void adjustPassengerRotation(){
        if(getFirstPassenger() instanceof AbstractMinecart){
            getFirstPassenger().setYRot(getYRot());
        }
        else if(getFirstPassenger() instanceof Boat){
            getFirstPassenger().setYRot(getYRot() + 90F);
        }
    }

    @Override
    public void ejectPassengers() {
        var passenger = getFirstPassenger();
        if (passenger != null) {
            passenger.stopRiding();
            var movement = getDeltaMovement();
            if(passenger instanceof Player || passenger.getPassengers().stream().anyMatch(entity -> entity instanceof Player)){
                movement.scale(2 + passenger.getBbWidth());
                passenger.setDeltaMovement(movement.x,movement.y,movement.z);
            } else passenger.setDeltaMovement(getDeltaMovement().scale(1.5));
        }
    }

    @Override
    public boolean canCollideWith(@NotNull Entity pEntity) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        currentT = pCompound.getFloat("CurrentT");
        currentSpeed = pCompound.getFloat("CurrentSpeed");
        if(pCompound.contains("NavigatorPos")){
            nextNode = NbtUtils.readBlockPos(pCompound.getCompound("NextNode"));
            var be = level().getBlockEntity(NbtUtils.readBlockPos(pCompound.getCompound("NavigatorPos")));
            if(be instanceof INavigationPipeBE navigationPipeBE){
                navigator = navigationPipeBE;
            }
        }
        if(pCompound.contains("BeforeEjectTick")){
            readyToEject = true;
            beforeEjectTick = pCompound.getInt("BeforeEjectTick");
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        pCompound.putFloat("CurrentT",currentT);
        pCompound.putFloat("CurrentSpeed",currentSpeed);
        if(navigator!=null){
            pCompound.put("NavigatorPos",NbtUtils.writeBlockPos(((BlockEntity) navigator).getBlockPos()));
            pCompound.put("NextNode",NbtUtils.writeBlockPos(nextNode));
        }
        if(readyToEject){
            pCompound.putInt("BeforeEjectTick",beforeEjectTick);
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
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
    }

    public static void modifyFOV(ComputeFovModifierEvent event){
        if(event.getPlayer().getVehicle() instanceof CarrierEntity carrier){
            float mul = ((carrier.getSyncCurrentSpeed() - getInitialSpeed()) / (carrier.getStandardSpeed() - getInitialSpeed())) * 1.5f + 1;
            event.setNewFovModifier(event.getNewFovModifier()*mul);
        }
    }
}
