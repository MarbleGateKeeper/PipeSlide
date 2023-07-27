package plus.dragons.pipeslide.content.pipes.specific.standard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import plus.dragons.pipeslide.content.pipes.PipeConnectionProviderRenderer;

public class PipeNodeRenderer extends PipeConnectionProviderRenderer<PipeNodeBlockEntity> {

    public PipeNodeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(PipeNodeBlockEntity te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light,
                              int overlay) {
        Level level = te.getLevel();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        poseStack.pushPose();
        poseStack.translate((te.getBlockPos().getX() % 100) * 0.0001, (te.getBlockPos().getY() % 100) * 0.0001, (te.getBlockPos().getZ() % 100) * 0.0001);
        te.getConnection().forEach(bc -> {
            poseStack.translate(0.001, 0.001, 0.001);
            renderConnection(level, te.getBlockPos(), bc, poseStack, vb, light, overlay);
        });
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull PipeNodeBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 96 * 2;
    }
}
