package plus.dragons.pipeslide.content.pipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.foundation.client.renderer.SafeBERenderer;

public abstract class PipeConnectionProviderRenderer<T extends BlockEntity & IPipeConnectionProviderBE> extends SafeBERenderer<T> {

    public static void renderConnection(Level level, BlockPos startFromPos, PipeConnection connection, PoseStack poseStack, VertexConsumer vb, int light,
                                        int overlay) {
        if (!connection.primaryForRender)
            return;

        poseStack.pushPose();
        poseStack.translate(0.5,0.5,0.5);
        if(connection.curveConnection==null){
            connection.style.fillPipeSegment(Vec3.atCenterOf(startFromPos),Vec3.atCenterOf(connection.to),
                    poseStack,vb,light,overlay);
        } else {
            Vec3 totalOffset = Vec3.ZERO;
            for(var data: connection.curveConnection.getSegmentRenderData()){
                poseStack.translate(0,0.0001,0);
                poseStack.pushPose();
                poseStack.translate(totalOffset.x,totalOffset.y,totalOffset.z);
                connection.style.fillPipeSegment(data.start, data.end, poseStack, vb, light, overlay);
                poseStack.popPose();
                totalOffset = totalOffset.add(data.start.vectorTo(data.end));
            }
        }
        poseStack.popPose();
    }
}
