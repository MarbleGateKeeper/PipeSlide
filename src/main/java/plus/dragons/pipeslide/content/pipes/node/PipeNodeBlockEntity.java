package plus.dragons.pipeslide.content.pipes.node;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import plus.dragons.pipeslide.foundation.blockentity.LazyTickBE;

import java.util.*;

public class PipeNodeBlockEntity extends LazyTickBE {

    Map<BlockPos, BezierConnection> connections;

    public PipeNodeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        connections = new HashMap<>();
        setLazyTickRate(100);
    }

    public Map<BlockPos, BezierConnection> getConnections() {
        return connections;
    }

    public void validateConnections() {
        Set<BlockPos> invalid = new HashSet<>();

        for (Map.Entry<BlockPos, BezierConnection> entry : connections.entrySet()) {
            BlockPos key = entry.getKey();
            BezierConnection bc = entry.getValue();

            if (!key.equals(bc.getKey()) || !worldPosition.equals(bc.tePositions.getFirst())) {
                invalid.add(key);
                continue;
            }

            BlockState blockState = level.getBlockState(key);
            if (blockState.getBlock()instanceof ITrackBlock trackBlock && !blockState.getValue(TrackBlock.HAS_TE))
                for (Vec3 v : trackBlock.getTrackAxes(level, key, blockState)) {
                    Vec3 bcEndAxis = bc.axes.getSecond();
                    if (v.distanceTo(bcEndAxis) < 1 / 1024f || v.distanceTo(bcEndAxis.scale(-1)) < 1 / 1024f)
                        level.setBlock(key, blockState.setValue(TrackBlock.HAS_TE, true), 3);
                }

            BlockEntity blockEntity = level.getBlockEntity(key);
            if (!(blockEntity instanceof TrackTileEntity trackTE) || blockEntity.isRemoved()) {
                invalid.add(key);
                continue;
            }

            if (!trackTE.connections.containsKey(worldPosition))
                trackTE.addConnection(bc.secondary());
        }

        for (BlockPos blockPos : invalid)
            removeConnection(blockPos);
    }

    public void addConnection(BezierConnection connection) {
        connections.put(connection.getKey(), connection);
        level.scheduleTick(worldPosition, getBlockState().getBlock(), 1);
        notifyUpdate();
    }

    public void removeConnection(BlockPos target) {
        BezierConnection removed = connections.remove(target);
        notifyUpdate();

        if (!connections.isEmpty() || getBlockState().getOptionalValue(TrackBlock.SHAPE)
                .orElse(TrackShape.NONE)
                .isPortal())
            return;

        BlockState blockState = level.getBlockState(worldPosition);
        if (blockState.hasProperty(TrackBlock.HAS_TE))
            level.setBlockAndUpdate(worldPosition, blockState.setValue(TrackBlock.HAS_TE, false));
        AllPackets.channel.send(packetTarget(), new RemoveTileEntityPacket(worldPosition));
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        writeTurns(tag);
    }

    private void writeTurns(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (BezierConnection bezierConnection : connections.values())
            listTag.add(bezierConnection.write(worldPosition));
        tag.put("Connections", listTag);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        connections.clear();
        for (Tag t : tag.getList("Connections", Tag.TAG_COMPOUND)) {
            if (!(t instanceof CompoundTag))
                return;
            BezierConnection connection = new BezierConnection((CompoundTag) t, worldPosition);
            connections.put(connection.getKey(), connection);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }


    public boolean hasInteractableConnections() {
        for (BezierConnection connection : connections.values())
            if (connection.isPrimary())
                return true;
        return false;
    }

}
