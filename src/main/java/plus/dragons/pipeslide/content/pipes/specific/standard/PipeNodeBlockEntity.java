package plus.dragons.pipeslide.content.pipes.specific.standard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.content.pipes.PipeConnection;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.content.pipes.IPipeConnectionProviderBE;
import plus.dragons.pipeslide.foundation.blockentity.LazyTickBE;

import java.util.ArrayList;
import java.util.List;

public class PipeNodeBlockEntity extends LazyTickBE implements IPipeConnectionProviderBE {
    @Nullable
    public PipeConnection connectionA;
    @Nullable
    public PipeConnection connectionB;

    public PipeNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_NODE.get(), pos, state);
        setLazyTickRate(100);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        if(this.connectionA!=null)
            tag.put("ConnectionA",connectionA.write());
        if(this.connectionB!=null)
            tag.put("ConnectionB",connectionB.write());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.connectionA = tag.contains("ConnectionA")? new PipeConnection(tag.getCompound("ConnectionA")): null;
        this.connectionB = tag.contains("ConnectionB")? new PipeConnection(tag.getCompound("ConnectionB")): null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public List<PipeConnection> getConnection() {
        List<PipeConnection> ret = new ArrayList<>();
        if(connectionA!=null) ret.add(connectionA);
        if(connectionB!=null) ret.add(connectionB);
        return ret;
    }
}
