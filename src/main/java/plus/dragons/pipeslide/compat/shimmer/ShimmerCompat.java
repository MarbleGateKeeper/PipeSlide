package plus.dragons.pipeslide.compat.shimmer;

import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.content.pipes.PipeConnection;

import java.util.function.Supplier;

public class ShimmerCompat {
    public static void renderConnection(Level level, BlockPos startFromPos, PipeConnection connection, PoseStack poseStack, VertexConsumer vb, int light,
                                                           int overlay){
        PoseStack finalStack = RenderUtils.copyPoseStack(poseStack); // we provide a way to copy the poststack
        PostProcessing.BLOOM_UNREAL.postEntityForce(bufferSource -> {  //must use the bufferSource provided by us
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.cutout()); //must use the bufferSource provided by us
            finalStack.pushPose();
            finalStack.translate(0.5, 0.5, 0.5);
            if (connection.curveConnection == null) {
                connection.style.fillPipeSegment(Vec3.atCenterOf(startFromPos), Vec3.atCenterOf(connection.to),
                        finalStack, consumer, light, overlay);
            } else {
                Vec3 totalOffset = Vec3.ZERO;
                for (var data : connection.curveConnection.getSegmentRenderData()) {
                    finalStack.translate(0.001, 0.001, 0.001);
                    finalStack.pushPose();
                    finalStack.translate(totalOffset.x, totalOffset.y, totalOffset.z);
                    connection.style.fillPipeSegment(data.start, data.end, finalStack, consumer, light, overlay);
                    finalStack.popPose();
                    totalOffset = totalOffset.add(data.start.vectorTo(data.end));
                }
            }
            finalStack.popPose();
        });
    }

    public static void renderPlatformIndicator(ItemRenderer itemRenderer, Supplier<ItemStack> itemStackSupplier, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay){
        PoseStack finalStack = RenderUtils.copyPoseStack(poseStack); // we provide a way to copy the poststack
        PostProcessing.BLOOM_UNREAL.postEntityForce(bufferSource -> {  //must use the bufferSource provided by us
            itemRenderer.renderStatic(itemStackSupplier.get(), ItemDisplayContext.FIXED, light, overlay, finalStack, bufferSource, Minecraft.getInstance().level, 0);
        });
    }
}
