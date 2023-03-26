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

    public PipeNodeRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(PipeNodeBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
                              int overlay) {
        Level level = te.getLevel();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        te.getConnection()
                .forEach(bc -> renderConnection(level, bc, ms, vb));
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
