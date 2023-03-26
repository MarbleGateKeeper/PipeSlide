package plus.dragons.pipeslide.content.pipes.style;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

public interface IPipeStyle {
    CompoundTag write();

    PipeStyleType<?> getType();

    void fillPipeSegment(Vec3 start, Vec3 end, Vec3 direction, PoseStack ms, VertexConsumer vb,int light,
                         int overlay);

    IPipeStyle EMPTY = new IPipeStyle() {
        public static final ResourceLocation END_PORTAL_LOCATION = new ResourceLocation("textures/entity/end_portal.png");
        @Override
        public CompoundTag write() {
            return new CompoundTag();
        }

        @Override
        public PipeStyleType<?> getType() {
            return PipeStyleType.DEFAULT;
        }

        @Override
        public void fillPipeSegment(Vec3 start, Vec3 end, Vec3 direction, PoseStack ms, VertexConsumer vb, int light,
                                    int overlay) {
            // TODO here is just a small test
            var matrix = ms.last().pose();
            final var atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(END_PORTAL_LOCATION);
            vb.vertex(matrix,0,0,0).color(1,1,1,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal(0,1,0).endVertex();
            vb.vertex(matrix,0,0,2).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV1())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal(0,1,0).endVertex();
            vb.vertex(matrix,0,2,2).color(0,1,0,1F).uv(atlas.getU1(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal(0,1,0).endVertex();
            vb.vertex(matrix,0,2,0).color(0,0,1,1F).uv(atlas.getU1(),atlas.getV1())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal(0,1,0).endVertex();
        }
    };
}
