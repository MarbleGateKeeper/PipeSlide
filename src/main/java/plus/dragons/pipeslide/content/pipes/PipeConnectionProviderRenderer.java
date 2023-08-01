package plus.dragons.pipeslide.content.pipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import plus.dragons.pipeslide.compat.shimmer.ShimmerCompat;
import plus.dragons.pipeslide.foundation.client.renderer.SafeBERenderer;

import java.util.function.Supplier;

public abstract class PipeConnectionProviderRenderer<T extends BlockEntity & IPipeConnectionProviderBE> extends SafeBERenderer<T> {

    private static Supplier<AlternativeRendering> alternativeRendering = null;

    static{
        if (ModList.get().isLoaded("shimmer")){
            alternativeRendering = ()->ShimmerCompat::renderConnection;
        }
    }
    public static void renderConnection(Level level, BlockPos startFromPos, PipeConnection connection, PoseStack poseStack, VertexConsumer vb, int light,
                                        int overlay) {
        if (!connection.primaryForRender)
            return;
        if (alternativeRendering!=null){
            alternativeRendering.get().render(level,startFromPos,connection,poseStack,vb,light,overlay);
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        if (connection.curveConnection == null) {
            connection.style.fillPipeSegment(Vec3.atCenterOf(startFromPos), Vec3.atCenterOf(connection.to),
                    poseStack, vb, light, overlay);
        } else {
            Vec3 totalOffset = Vec3.ZERO;
            for (var data : connection.curveConnection.getSegmentRenderData()) {
                poseStack.translate(0.001, 0.001, 0.001);
                poseStack.pushPose();
                poseStack.translate(totalOffset.x, totalOffset.y, totalOffset.z);
                connection.style.fillPipeSegment(data.start, data.end, poseStack, vb, light, overlay);
                poseStack.popPose();
                totalOffset = totalOffset.add(data.start.vectorTo(data.end));
            }
        }
        poseStack.popPose();
    }

    interface AlternativeRendering{
        void render(Level level, BlockPos startFromPos, PipeConnection connection, PoseStack poseStack, VertexConsumer vb, int light,
                                   int overlay);
    }
}
