package plus.dragons.pipeslide.content.pipes.specific.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectRenderer;

import java.util.function.Supplier;

public class PipePlatformWithIndicatorRenderer<T extends PipePlatformBlockEntity<? extends Entity>> extends PipeDoubleConnectRenderer<T> {

    private final Supplier<ItemStack> itemStackSupplier;
    private final ItemRenderer itemRenderer;
    private final BlockEntityRenderDispatcher dispatcher;

    public PipePlatformWithIndicatorRenderer(BlockEntityRendererProvider.Context context, Supplier<ItemStack> itemStackSupplier) {
        super(context);
        this.itemStackSupplier = itemStackSupplier;
        this.itemRenderer = context.getItemRenderer();
        this.dispatcher = context.getBlockEntityRenderDispatcher();
    }

    @Override
    protected void renderSafe(T te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, poseStack, buffer, light, overlay);
        poseStack.pushPose();
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.scale(0.7f, 0.7f, 0.7f);
        poseStack.mulPose(this.dispatcher.camera.rotation());
        itemRenderer.renderStatic(itemStackSupplier.get(), ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, Minecraft.getInstance().level, 0);
        poseStack.popPose();
    }
}
