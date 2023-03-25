package plus.dragons.pipeslide.content.pipes.node;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import plus.dragons.pipeslide.foundation.blockentity.renderer.SafeBERenderer;

public class PipeNodeRenderer extends SafeBERenderer<PipeNodeBlockEntity> {

    public PipeNodeRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(PipeNodeBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

    }
}
