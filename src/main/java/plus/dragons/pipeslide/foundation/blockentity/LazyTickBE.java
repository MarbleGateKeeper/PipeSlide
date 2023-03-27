package plus.dragons.pipeslide.foundation.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.tileEntity.SmartTileEntity
 */
public abstract class LazyTickBE extends CachedRenderBoundingBoxBE{

    private boolean initialized = false;
    private boolean firstNbtRead = true;
    protected int lazyTickRate;
    protected int lazyTickCounter;
    private boolean chunkUnloaded;

    // Used for simulating this TE in a client-only setting
    private boolean virtualMode;

    public LazyTickBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }

    public void initialize() {
        if (firstNbtRead) {
            firstNbtRead = false;
        }

        lazyTick();
    }

    public void tick() {
        if (!initialized && hasLevel()) {
            initialize();
            initialized = true;
        }

        if (lazyTickCounter-- <= 0) {
            lazyTickCounter = lazyTickRate;
            lazyTick();
        }
    }

    public void lazyTick() {}

    /**
     * Hook only these in future subclasses of LTBE
     */
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.saveAdditional(tag);
    }

    /**
     * Hook only these in future subclasses of LTBE
     */
    protected void read(CompoundTag tag, boolean clientPacket) {
        if (firstNbtRead) {
            firstNbtRead = false;
        }
        super.load(tag);
    }

    @Override
    public final void load(CompoundTag tag) {
        read(tag, false);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        chunkUnloaded = true;
    }

    @Override
    public final void setRemoved() {
        super.setRemoved();
        invalidate();
    }

    /**
     * Block destroyed or Chunk unloaded. Usually invalidates capabilities
     */
    public void invalidate() {}

    /**
     * Block destroyed or replaced. Requires Block to call ITE::onRemove
     */
    public void destroy() {}

    @Override
    public final void saveAdditional(CompoundTag tag) {
        write(tag, false);
    }

    @Override
    public final void readClient(CompoundTag tag) {
        read(tag, true);
    }

    @Override
    public final CompoundTag writeClient(CompoundTag tag) {
        write(tag, true);
        return tag;
    }

    public void setLazyTickRate(int slowTickRate) {
        this.lazyTickRate = slowTickRate;
        this.lazyTickCounter = slowTickRate;
    }

    public void markVirtual() {
        virtualMode = true;
    }

    public boolean isVirtual() {
        return virtualMode;
    }

    public boolean isChunkUnloaded() {
        return chunkUnloaded;
    }

    public boolean canPlayerUse(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this)
            return false;
        return player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    public void sendToContainer(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(getBlockPos());
        buffer.writeNbt(getUpdateTag());
    }

    protected boolean isItemHandlerCap(Capability<?> cap) {
        return cap == ForgeCapabilities.ITEM_HANDLER;
    }

    protected boolean isFluidHandlerCap(Capability<?> cap) {
        return cap == ForgeCapabilities.FLUID_HANDLER;
    }

    public static class Ticker<T extends BlockEntity> implements BlockEntityTicker<T> {

        @Override
        public void tick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
            if (!pBlockEntity.hasLevel())
                pBlockEntity.setLevel(pLevel);
            ((LazyTickBE) pBlockEntity).tick();
        }

    }


}