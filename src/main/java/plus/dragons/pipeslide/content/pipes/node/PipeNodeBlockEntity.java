package plus.dragons.pipeslide.content.pipes.node;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.foundation.blockentity.LazyTickBE;

public class PipeNodeBlockEntity extends LazyTickBE {
    @Nullable
    public BlockPos nextNodeA;
    @Nullable
    public BezierConnection nodeACurve;
    @Nullable
    public BlockPos nextNodeB;
    @Nullable
    public BezierConnection nodeBCurve;


    public PipeNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_NODE.get(), pos, state);
        setLazyTickRate(100);
        this.nextNodeA = null;
        this.nodeACurve = null;
        this.nextNodeB = null;
        this.nodeBCurve = null;
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        // TODO
        /*ListTag listTag = new ListTag();
        for (BezierConnection bezierConnection : connections.values())
            listTag.add(bezierConnection.write(worldPosition));
        tag.put("Connections", listTag);*/
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        // TODO
        /*connections.clear();
        for (Tag t : tag.getList("Connections", Tag.TAG_COMPOUND)) {
            if (!(t instanceof CompoundTag))
                return;
            BezierConnection connection = new BezierConnection((CompoundTag) t, worldPosition);
            connections.put(connection.getKey(), connection);
        }*/
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

}
