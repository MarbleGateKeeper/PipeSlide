package plus.dragons.pipeslide.content.pipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.foundation.client.renderer.SafeBERenderer;
import plus.dragons.pipeslide.foundation.utility.AngleHelper;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

public abstract class PipeConnectionProviderRenderer<T extends BlockEntity & IPipeConnectionProviderBE> extends SafeBERenderer<T> {

    public static void renderConnection(Level level, PipeConnection connection, PoseStack ms, VertexConsumer vb) {
        if (!connection.primaryForRender)
            return;

        ms.pushPose();

        if(connection.curveConnection==null){

        } else {
            // TODO
            BlockPos tePosition = connection.to;
            BezierConnection.SegmentAngles[] segments = connection.curveConnection.getBakedSegments();

            for (int i = 1; i < segments.length; i++) {
                BezierConnection.SegmentAngles segment = segments[i];
                int light = LevelRenderer.getLightColor(level, segment.lightPosition.offset(tePosition));
            }
        }
        ms.popPose();
    }

    public static Vec3 getModelAngles(Vec3 normal, Vec3 diff) {
        double diffX = diff.x();
        double diffY = diff.y();
        double diffZ = diff.z();
        double len = Mth.sqrt((float) (diffX * diffX + diffZ * diffZ));
        double yaw = Mth.atan2(diffX, diffZ);
        double pitch = Mth.atan2(len, diffY) - Math.PI * .5;

        Vec3 yawPitchNormal = VecHelper.rotate(VecHelper.rotate(new Vec3(0, 1, 0), AngleHelper.deg(pitch), Direction.Axis.X),
                AngleHelper.deg(yaw), Direction.Axis.Y);

        double signum = Math.signum(yawPitchNormal.dot(normal));
        if (Math.abs(signum) < 0.5f)
            signum = yawPitchNormal.distanceToSqr(normal) < 0.5f ? -1 : 1;
        double dot = diff.cross(normal)
                .normalize()
                .dot(yawPitchNormal);
        double roll = Math.acos(Mth.clamp(dot, -1, 1)) * signum;
        return new Vec3(pitch, yaw, roll);
    }
}
