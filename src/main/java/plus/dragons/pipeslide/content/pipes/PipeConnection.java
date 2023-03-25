package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.content.pipes.node.BezierConnection;

public class PipeConnection {

    public BlockPos to;
    public IPipeStyle style;
    @Nullable
    public BezierConnection curveConnection;


    public PipeConnection(CompoundTag compound) {
        this.to = NbtUtils.readBlockPos(compound.getCompound("TargetPos"));
        var styleType = new ResourceLocation(compound.getString("StyleType"));
        this.style = PipeStyleType.TYPES.get(styleType).fromTag(compound.getCompound("Style"));
        // TODO
    }

    public PipeConnection(BlockPos to) {
        this.to = to;
        this.style = IPipeStyle.EMPTY;
        this.curveConnection = null;
    }

    public CompoundTag write() {
        CompoundTag compound = new CompoundTag();
        compound.put("TargetPos", NbtUtils.writeBlockPos(to));
        compound.putString("StyleType",style.getType().getId().toString());
        compound.put("Style",style.write());
        // TODO
        return compound;
    }
}
