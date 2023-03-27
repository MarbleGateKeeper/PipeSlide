package plus.dragons.pipeslide.foundation.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;


public class BlandEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    public BlandEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T pEntity) {
        return new ResourceLocation("minecraft","entity/creeper");
    }
}
