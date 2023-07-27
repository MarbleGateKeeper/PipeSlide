package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.content.pipes.style.IPipeStyle;
import plus.dragons.pipeslide.content.pipes.style.MarbleWhiteStyle;
import plus.dragons.pipeslide.content.pipes.style.PipeStyleType;

public class PipeConnection {

    public BlockPos to;
    public IPipeStyle style;
    @Nullable
    public BezierConnection curveConnection;
    public boolean primaryForRender;


    public PipeConnection(CompoundTag compound) {
        this.to = NbtUtils.readBlockPos(compound.getCompound("TargetPos"));
        var styleType = new ResourceLocation(compound.getString("StyleType"));
        this.style = PipeStyleType.TYPES.get(styleType).fromTag(compound.getCompound("Style"));
        this.curveConnection = compound.contains("Curve") ? new BezierConnection(compound.getCompound("Curve")) : null;
        this.primaryForRender = compound.getBoolean("PrimaryForRender");
    }

    public PipeConnection(BlockPos to) {
        this.to = to;
        this.style = MarbleWhiteStyle.INSTANCE;
        this.curveConnection = null;
        this.primaryForRender = false;
    }

    public CompoundTag write() {
        CompoundTag compound = new CompoundTag();
        compound.put("TargetPos", NbtUtils.writeBlockPos(to));
        compound.putString("StyleType", style.getType().getId().toString());
        compound.put("Style", style.write());
        if (curveConnection != null)
            compound.put("Curve", curveConnection.write());
        compound.putBoolean("PrimaryForRender", primaryForRender);
        return compound;
    }
}
