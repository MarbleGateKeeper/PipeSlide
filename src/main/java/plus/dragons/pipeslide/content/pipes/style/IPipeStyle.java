package plus.dragons.pipeslide.content.pipes.style;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public interface IPipeStyle {
    CompoundTag write();

    PipeStyleType<?> getType();

    void fillPipeSegment(Vec3 start, Vec3 end, PoseStack poseStack, VertexConsumer consumer,int light,
                         int overlay);
}
