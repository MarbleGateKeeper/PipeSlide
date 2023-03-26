package plus.dragons.pipeslide.content.pipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.foundation.client.renderer.SafeBERenderer;

public abstract class PipeConnectionProviderRenderer<T extends BlockEntity & IPipeConnectionProviderBE> extends SafeBERenderer<T> {

    public static void renderConnection(Level level, BlockPos startFromPos, PipeConnection connection, PoseStack ms, VertexConsumer vb, int light,
                                        int overlay) {
        if (!connection.primaryForRender)
            return;

        ms.pushPose();
        if(connection.curveConnection==null){
            connection.style.fillPipeSegment(Vec3.atCenterOf(startFromPos),Vec3.atCenterOf(connection.to),
                    Vec3.atCenterOf(startFromPos).vectorTo(Vec3.atCenterOf(connection.to)).normalize(),
                    ms,vb,light,overlay);
        } else {
            for(var data: connection.curveConnection.getSegmentRenderData()){
                connection.style.fillPipeSegment(data.start,data.end, data.direction, ms,vb,light,overlay);
            }
        }
        ms.popPose();
    }
}
