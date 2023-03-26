package plus.dragons.pipeslide.content.pipes.style;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

public interface IPipeStyle {
    CompoundTag write();

    PipeStyleType<?> getType();

    void fillPipeSegment(Vec3 start, Vec3 end, Vec3 direction, PoseStack poseStack, VertexConsumer consumer,int light,
                         int overlay);

    IPipeStyle EMPTY = new IPipeStyle() {
        @Override
        public CompoundTag write() {
            return new CompoundTag();
        }

        @Override
        public PipeStyleType<?> getType() {
            return PipeStyleType.DEFAULT;
        }

        @Override
        public void fillPipeSegment(Vec3 start, Vec3 end, Vec3 direction, PoseStack poseStack, VertexConsumer consumer, int light,
                                    int overlay) {

            var matrix = poseStack.last().pose();
            var atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(IClientFluidTypeExtensions.of(Fluids.WATER.defaultFluidState()).getFlowingTexture());

            Vec3 absEnd = start.vectorTo(end);

            // calculate vertex
            double extend = 0.2;
            double width = 0.24;
            Vec3 frontFaceNormal = direction.scale(-1).normalize();
            Vec3 leftFaceNormal = VecHelper.rotate(direction,-90, Direction.Axis.Y).normalize();
            Vec3 rightFaceNormal = VecHelper.rotate(direction,90, Direction.Axis.Y).normalize();
            Vec3 upFaceNormal = direction.cross(leftFaceNormal).normalize();
            Vec3 downFaceNormal = direction.cross(rightFaceNormal).normalize();

            Vec3 extendFront = frontFaceNormal.scale(extend);
            Vec3 extendBack = direction.scale(extend);
            Vec3 upWidth = upFaceNormal.scale(width);
            Vec3 downWidth = downFaceNormal.scale(width);
            Vec3 leftWidth = leftFaceNormal.scale(width);
            Vec3 rightWidth = rightFaceNormal.scale(width);

            Vec3 front1 = Vec3.ZERO.add(extendFront).add(upWidth).add(rightWidth);
            Vec3 front2 = Vec3.ZERO.add(extendFront).add(upWidth).add(leftWidth);
            Vec3 front3 = Vec3.ZERO.add(extendFront).add(downWidth).add(leftWidth);
            Vec3 front4 = Vec3.ZERO.add(extendFront).add(downWidth).add(rightWidth);
            Vec3 back1 = absEnd.add(extendBack).add(upWidth).add(rightWidth);
            Vec3 back2 = absEnd.add(extendBack).add(upWidth).add(leftWidth);
            Vec3 back3 = absEnd.add(extendBack).add(downWidth).add(leftWidth);
            Vec3 back4 = absEnd.add(extendBack).add(downWidth).add(rightWidth);

            // build front face
            consumer.vertex(matrix, (float) front1.x, (float) front1.y, (float) front1.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) frontFaceNormal.x, (float) frontFaceNormal.y, (float) frontFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) front2.x, (float) front2.y, (float) front2.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) frontFaceNormal.x, (float) frontFaceNormal.y, (float) frontFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) front3.x, (float) front3.y, (float) front3.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) frontFaceNormal.x, (float) frontFaceNormal.y, (float) frontFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) front4.x, (float) front4.y, (float) front4.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) frontFaceNormal.x, (float) frontFaceNormal.y, (float) frontFaceNormal.z).endVertex();

            // build back face
            consumer.vertex(matrix, (float) back2.x, (float) back2.y, (float) back2.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) direction.x, (float) direction.y, (float) direction.z).endVertex();
            consumer.vertex(matrix, (float) back1.x, (float) back1.y, (float) back1.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) direction.x, (float) direction.y, (float) direction.z).endVertex();
            consumer.vertex(matrix, (float) back4.x, (float) back4.y, (float) back4.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) direction.x, (float) direction.y, (float) direction.z).endVertex();
            consumer.vertex(matrix, (float) back3.x, (float) back3.y, (float) back3.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) direction.x, (float) direction.y, (float) direction.z).endVertex();

            // build up face
            consumer.vertex(matrix, (float) front1.x, (float) front1.y, (float) front1.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) upFaceNormal.x, (float) upFaceNormal.y, (float) upFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) back1.x, (float) back1.y, (float) back1.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) upFaceNormal.x, (float) upFaceNormal.y, (float) upFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) back2.x, (float) back2.y, (float) back2.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) upFaceNormal.x, (float) upFaceNormal.y, (float) upFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) front2.x, (float) front2.y, (float) front2.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) upFaceNormal.x, (float) upFaceNormal.y, (float) upFaceNormal.z).endVertex();

            // build down face
            consumer.vertex(matrix, (float) front3.x, (float) front3.y, (float) front3.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) downFaceNormal.x, (float) downFaceNormal.y, (float) downFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) back3.x, (float) back3.y, (float) back3.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) downFaceNormal.x, (float) downFaceNormal.y, (float) downFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) back4.x, (float) back4.y, (float) back4.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) downFaceNormal.x, (float) downFaceNormal.y, (float) downFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) front4.x, (float) front4.y, (float) front4.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) downFaceNormal.x, (float) downFaceNormal.y, (float) downFaceNormal.z).endVertex();

            // build left face
            consumer.vertex(matrix, (float) back1.x, (float) back1.y, (float) back1.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) leftFaceNormal.x, (float) leftFaceNormal.y, (float) leftFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) front1.x, (float) front1.y, (float) front1.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) leftFaceNormal.x, (float) leftFaceNormal.y, (float) leftFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) front4.x, (float) front4.y, (float) front4.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) leftFaceNormal.x, (float) leftFaceNormal.y, (float) leftFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) back4.x, (float) back4.y, (float) back4.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) leftFaceNormal.x, (float) leftFaceNormal.y, (float) leftFaceNormal.z).endVertex();

            // build right face
            consumer.vertex(matrix, (float) front2.x, (float) front2.y, (float) front2.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) rightFaceNormal.x, (float) rightFaceNormal.y, (float) rightFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) back2.x, (float) back2.y, (float) back2.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) rightFaceNormal.x, (float) rightFaceNormal.y, (float) rightFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) back3.x, (float) back3.y, (float) back3.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) rightFaceNormal.x, (float) rightFaceNormal.y, (float) rightFaceNormal.z).endVertex();
            consumer.vertex(matrix, (float) front3.x, (float) front3.y, (float) front3.z).color(1,0,0,1F).uv(atlas.getU0(),atlas.getV0())
                    .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                    .normal((float) rightFaceNormal.x, (float) rightFaceNormal.y, (float) rightFaceNormal.z).endVertex();
        }
    };
}
