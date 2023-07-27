package plus.dragons.pipeslide.content.pipes.specific.standard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.content.pipes.*;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

import java.util.ArrayList;
import java.util.List;

public class PipeNodeBlockEntity extends NavigatingBE implements IPipeConnectionProviderBE {
    @Nullable
    public PipeConnection connectionA;
    @Nullable
    public PipeConnection connectionB;

    public PipeNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_NODE.get(), pos, state);
        setLazyTickRate(20);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!level.isClientSide && (connectionA != null || connectionB != null)) {
            level.getEntitiesOfClass(Player.class, new AABB(getBlockPos()).expandTowards(0, 1, 0), player -> {
                return !player.isSpectator() & player.getVehicle() == null;
            }).forEach(this::startPipeRide);
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
        var carrier = new PlayerCarrierEntity(level, this, next);
        carrier.setPos(VecHelper.getCenterOf(getBlockPos()));
        level.addFreshEntity(carrier);
        player.startRiding(carrier, true);
    }

    @Override
    public List<PipeConnection> getConnection() {
        List<PipeConnection> ret = new ArrayList<>();
        if (connectionA != null) ret.add(connectionA);
        if (connectionB != null) ret.add(connectionB);
        return ret;
    }

    @Override
    public void destroy() {
        if (connectionA != null) {
            if (level.getBlockState(connectionA.to).getBlock() instanceof IPipeConnectableBlock pipe)
                pipe.removePipeConnection(level, connectionA.to, getBlockPos());
        }
        if (connectionB != null) {
            if (level.getBlockState(connectionB.to).getBlock() instanceof IPipeConnectableBlock pipe)
                pipe.removePipeConnection(level, connectionB.to, getBlockPos());
        }
    }

    @Override
    public Result navigate(PlayerCarrierEntity carrier, BlockPos nextNode, float speed, float currentT) {
        var connection = pickConnection(nextNode);

        if (connection == null) return new Result(null, nextNode, speed, currentT);

        Vec3 position = new Vec3(carrier.getX(), carrier.getY(), carrier.getZ());
        Vec3 nextVec3 = VecHelper.getCenterOf(nextNode);
        double leftLength = connection.curveConnection == null ? Math.sqrt(getBlockPos().distSqr(nextNode)) * (1 - currentT) : connection.curveConnection.getLength() * (1 - currentT);
        // travelling can be handled in this navigator
        if (leftLength > speed) {
            // linear connection
            if (connection.curveConnection == null) {
                Vec3 newPos = VecHelper.lerp((float) (speed / leftLength), position, nextVec3);
                carrier.setPos(newPos);
                carrier.setDeltaMovement(position.vectorTo(nextVec3).normalize().scale(speed));
                return new Result(this, nextNode, speed, (float) (1 - (newPos.vectorTo(nextVec3).length() / Math.sqrt(getBlockPos().distSqr(nextNode)))));
            }
            // curve connection
            else {
                var curve = connection.curveConnection;
                float newT = (float) Math.min(currentT + speed / curve.getLength(), 0.999);
                carrier.setPos(curve.getPosition(newT));
                carrier.setDeltaMovement(curve.getDirection(newT));
                return new Result(this, nextNode, speed, newT);
            }
        }
        // travelling should be handled in next navigator
        else {
            if (level.getBlockEntity(nextNode) instanceof INavigationPipeBE nextNavigator) {
                carrier.setPos(nextVec3);
                var nextNextNode = nextNavigator.getNextNode(getBlockPos());
                carrier.setPos(nextVec3);


                if (nextNextNode == null) {
                    // No next node
                    return new Result(null, null, speed, 1);
                } else {
                    return nextNavigator.navigate(carrier, nextNextNode, speed, 0);
                }

            } else {
                return new Result(null, nextNode, speed, currentT);
            }
        }

    }


    @Override
    public @Nullable BlockPos getNextNode(BlockPos from) {
        if (connectionA != null && connectionA.to.equals(from))
            return connectionB == null ? null : connectionB.to;
        else if (connectionB != null && connectionB.to.equals(from))
            return connectionA == null ? null : connectionA.to;
        else return null;
    }

    @Nullable
    private PipeConnection pickConnection(BlockPos next) {
        if (connectionA != null && connectionA.to.equals(next))
            return connectionA;
        else if (connectionB != null && connectionB.to.equals(next))
            return connectionB;
        else return null;
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        if (this.connectionA != null)
            tag.put("ConnectionA", connectionA.write());
        if (this.connectionB != null)
            tag.put("ConnectionB", connectionB.write());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.connectionA = tag.contains("ConnectionA") ? new PipeConnection(tag.getCompound("ConnectionA")) : null;
        this.connectionB = tag.contains("ConnectionB") ? new PipeConnection(tag.getCompound("ConnectionB")) : null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
