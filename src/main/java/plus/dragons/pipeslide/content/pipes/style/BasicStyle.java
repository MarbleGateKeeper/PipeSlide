package plus.dragons.pipeslide.content.pipes.style;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

public class BasicStyle implements IPipeStyle {
    private static final ResourceLocation WHITE_CONCRETE = new ResourceLocation("block/white_concrete");
    public static BasicStyle createDefault(){
        return new BasicStyle();
    }

    @Override
    public CompoundTag write() {
        return new CompoundTag();
    }

    @Override
    public PipeStyleType<?> getType() {
        return PipeStyleType.DEFAULT;
    }

    @Override
    public void fillPipeSegment(Vec3 start, Vec3 end, PoseStack poseStack, VertexConsumer consumer, int light,
                                int overlay) {

        var direction = start.vectorTo(end).normalize();
        var matrix = poseStack.last().pose();
        var atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(WHITE_CONCRETE);

        Vec3 absEnd = start.vectorTo(end);

        // calculate vertex and vertex normal
        float extend = 0.2f;
        float width = 0.24f;
        float length = (float) absEnd.length();

        Vec3 frontFaceNormal = direction.scale(-1);
        Vec3 downFaceNormal = frontFaceNormal.cross(VecHelper.axisAlingedPlaneOf(Direction.DOWN)).normalize().cross(frontFaceNormal).normalize();
        if(downFaceNormal.length()<0.8f){
            downFaceNormal = frontFaceNormal.cross(VecHelper.axisAlingedPlaneOf(Direction.WEST)).normalize().cross(frontFaceNormal).normalize();
        }
        Vec3 upFaceNormal = downFaceNormal.scale(-1).normalize();
        Vec3 rightFaceNormal = frontFaceNormal.cross(downFaceNormal).normalize();
        Vec3 leftFaceNormal = rightFaceNormal.scale(-1);

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

        fillFrontQuad(consumer, matrix, atlas, front1, front2, front3, front4, frontFaceNormal, light);
        fillFrontQuad(consumer, matrix, atlas, back2, back1, back4, back3, direction, light);

        fillSideQuad(consumer, matrix, atlas, front1, back1, back2, front2, direction, upFaceNormal, width, length, light);
        fillSideQuad(consumer, matrix, atlas, front3, back3, back4, front4, direction, downFaceNormal, width, length, light);
        fillSideQuad(consumer, matrix, atlas, back1, front1, front4, back4, frontFaceNormal, leftFaceNormal, width, length, light);
        fillSideQuad(consumer, matrix, atlas, front2, back2, back3, front3, direction, rightFaceNormal, width, length, light);
    }

    private void fillFrontQuad(VertexConsumer consumer, Matrix4f matrix, TextureAtlasSprite atlas, Vec3 p1, Vec3 p2, Vec3 p3, Vec3 p4, Vec3 faceNormal, int light) {
        draw(consumer, matrix, atlas, p1, p4, faceNormal, light, p2, p3);
    }

    private void fillSideQuad(VertexConsumer consumer, Matrix4f matrix, TextureAtlasSprite atlas, Vec3 p1, Vec3 finalP2, Vec3 finalP3, Vec3 p4, Vec3 direction, Vec3 faceNormal, float width, float length, int light) {
        float textureRatio = (atlas.getU1() - atlas.getU0()) / (atlas.getV1() - atlas.getV0());
        float ratio = length / width;
        float sideSegments = ratio / textureRatio;
        float unitLength = length / sideSegments;
        Vec3 p2 = p1.add(direction.scale(unitLength));
        Vec3 p3 = p4.add(direction.scale(unitLength));
        while (sideSegments > 0) {
            draw(consumer, matrix, atlas, p1, p4, faceNormal, light, p2, p3);
            sideSegments -= 1;
            if (sideSegments > 0) {
                p1 = p1.add(direction.scale(unitLength));
                p4 = p4.add(direction.scale(unitLength));
                if (sideSegments > 1) {
                    p2 = p2.add(direction.scale(unitLength));
                    p3 = p3.add(direction.scale(unitLength));
                } else {
                    p2 = finalP2;
                    p3 = finalP3;
                }
            }
        }
    }

    private void draw(VertexConsumer consumer, Matrix4f matrix, TextureAtlasSprite atlas, Vec3 p1, Vec3 p4, Vec3 faceNormal, int light, Vec3 p2, Vec3 p3) {
        consumer.vertex(matrix, (float) p1.x, (float) p1.y, (float) p1.z).color(1, 1, 1, 1F).uv(atlas.getU0(), atlas.getV0())
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal((float) faceNormal.x, (float) faceNormal.y, (float) faceNormal.z).endVertex();
        consumer.vertex(matrix, (float) p2.x, (float) p2.y, (float) p2.z).color(1, 1, 1, 1F).uv(atlas.getU0(), atlas.getV1())
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal((float) faceNormal.x, (float) faceNormal.y, (float) faceNormal.z).endVertex();
        consumer.vertex(matrix, (float) p3.x, (float) p3.y, (float) p3.z).color(1, 1, 1, 1F).uv(atlas.getU1(), atlas.getV1())
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal((float) faceNormal.x, (float) faceNormal.y, (float) faceNormal.z).endVertex();
        consumer.vertex(matrix, (float) p4.x, (float) p4.y, (float) p4.z).color(1, 1, 1, 1F).uv(atlas.getU1(), atlas.getV0())
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal((float) faceNormal.x, (float) faceNormal.y, (float) faceNormal.z).endVertex();
    }
}
