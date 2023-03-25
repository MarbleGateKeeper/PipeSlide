package plus.dragons.pipeslide.content.pipes.node;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.foundation.utility.Couple;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

import java.util.Iterator;

public class BezierConnection implements Iterable<BezierConnection.Segment> {

    public Couple<BlockPos> endPoints;
    public BlockPos midPoint;

    // runtime
    private boolean resolved;
    private double length;
    private float[] stepLUT;
    private int segments;

    private AABB bounds;

    public BezierConnection(Couple<BlockPos> endPoints, BlockPos midPoint) {
        this.endPoints = endPoints;
        this.midPoint = midPoint;
        this.resolved = false;
    }

    public BezierConnection revert() {
        return new BezierConnection(endPoints.swap(), midPoint);
    }

    public BezierConnection(CompoundTag compound) {
        this(Couple.deserializeEach(compound.getList("EndPoints", Tag.TAG_COMPOUND), NbtUtils::readBlockPos),
                NbtUtils.readBlockPos(compound.getCompound("MidPoint")));
    }

    public CompoundTag write(BlockPos localTo) {
        CompoundTag compound = new CompoundTag();
        compound.put("EndPoints", endPoints.serializeEach(NbtUtils::writeBlockPos));
        compound.put("MidPoint", NbtUtils.writeBlockPos(midPoint));
        return compound;
    }

    public BezierConnection(FriendlyByteBuf buffer) {
        this(Couple.create(buffer::readBlockPos), buffer.readBlockPos());
    }

    public void write(FriendlyByteBuf buffer) {
        endPoints.forEach(buffer::writeBlockPos);
        buffer.writeBlockPos(midPoint);
    }

    public BlockPos getKey() {
        return endPoints.getSecond();
    }

    // Runtime information
    public double getLength() {
        resolve();
        return length;
    }

    public float[] getStepLUT() {
        resolve();
        return stepLUT;
    }

    public int getSegmentCount() {
        resolve();
        return segments;
    }

    public Vec3 getPosition(double t) {
        resolve();
        return VecHelper.bezier(VecHelper.getCenterOf(endPoints.getFirst()), VecHelper.getCenterOf(endPoints.getSecond()), VecHelper.getCenterOf(midPoint), (float) t);
    }

    public float getSegmentT(int index) {
        return index == segments ? 1 : index * stepLUT[index] / segments;
    }

    public double incrementT(double currentT, double distance) {
        resolve();
        double dx =
                VecHelper.bezierDerivative(VecHelper.getCenterOf(endPoints.getFirst()), VecHelper.getCenterOf(endPoints.getSecond()), VecHelper.getCenterOf(midPoint), (float) currentT)
                        .length() / getLength();
        return currentT + distance / dx;

    }

    public AABB getBounds() {
        resolve();
        return bounds;
    }

    public Vec3 getNormal(double t) {
        resolve();
        Vec3 end1 = VecHelper.getCenterOf(endPoints.getFirst());
        Vec3 end2 = VecHelper.getCenterOf(endPoints.getSecond());
        Vec3 mid = VecHelper.getCenterOf(midPoint);

        Vec3 derivative = VecHelper.bezierDerivative(end1, end2, mid, (float) t).normalize();
        return derivative.cross(derivative.cross(new Vec3(0,1,0)).normalize()).normalize();
    }

    private void resolve() {
        if (resolved)
            return;
        resolved = true;

        Vec3 end1 = VecHelper.getCenterOf(endPoints.getFirst());
        Vec3 end2 = VecHelper.getCenterOf(endPoints.getSecond());
        Vec3 mid = VecHelper.getCenterOf(midPoint);

        int scanCount = 16;
        length = 0;

        Vec3 previous1 = end1;
        for (int i = 0; i <= scanCount; i++) {
            float t = i / (float) scanCount;
            Vec3 result = VecHelper.bezier(end1, end2, mid, t);
            if (previous1 != null)
                length += result.distanceTo(previous1);
            previous1 = result;
        }

        segments = (int) (length * 2);
        stepLUT = new float[segments + 1];
        stepLUT[0] = 1;
        float combinedDistance = 0;

        bounds = new AABB(end1, end2);

        // determine step lut
        Vec3 previous2 = end1;
        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            Vec3 result = VecHelper.bezier(end1, end2, mid, t);
            bounds = bounds.minmax(new AABB(result, result));
            if (i > 0) {
                combinedDistance += result.distanceTo(previous2) / length;
                stepLUT[i] = t / combinedDistance;
            }
            previous2 = result;
        }

        bounds = bounds.inflate(1.375f);
    }

    @Override
    public Iterator<Segment> iterator() {
        resolve();
        return new Bezierator(this);
    }

    /*public int getTrackItemCost() {
        return (getSegmentCount() + 1) / 2;
    }*/

    public void spawnDestroyParticles(Level level) {
        // TODO
        /*BlockParticleOption data = new BlockParticleOption(ParticleTypes.BLOCK, AllBlocks.TRACK.getDefaultState());
        BlockParticleOption girderData =
                new BlockParticleOption(ParticleTypes.BLOCK, AllBlocks.METAL_GIRDER.getDefaultState());
        if (!(level instanceof ServerLevel slevel))
            return;
        Vec3 origin = Vec3.atLowerCornerOf(tePositions.getFirst());
        for (Segment segment : this) {
            for (int offset : Iterate.positiveAndNegative) {
                Vec3 v = segment.position.add(segment.normal.scale(14 / 16f * offset))
                        .add(origin);
                slevel.sendParticles(data, v.x, v.y, v.z, 1, 0, 0, 0, 0);
                if (!hasGirder)
                    continue;
                slevel.sendParticles(girderData, v.x, v.y - .5f, v.z, 1, 0, 0, 0, 0);
            }
        }*/
    }

    public static class Segment {

        public int index;
        public Vec3 position;
        public Vec3 derivative;
        public Vec3 normal;

    }

    private static class Bezierator implements Iterator<Segment> {

        private final BezierConnection bc;
        private final Segment segment;
        private final Vec3 end1;
        private final Vec3 end2;
        private final Vec3 mid;

        private Bezierator(BezierConnection bc) {
            bc.resolve();
            this.bc = bc;

            end1 = VecHelper.getCenterOf(bc.endPoints.getFirst());
            end2 = VecHelper.getCenterOf(bc.endPoints.getFirst());
            mid = VecHelper.getCenterOf(bc.midPoint);

            segment = new Segment();
            segment.index = -1; // will get incremented to 0 in #next()
        }

        @Override
        public boolean hasNext() {
            return segment.index + 1 <= bc.segments;
        }

        @Override
        public Segment next() {
            segment.index++;
            float t = this.bc.getSegmentT(segment.index);
            segment.position = VecHelper.bezier(end1, end2, mid, t);
            segment.derivative = VecHelper.bezierDerivative(end1, end2, mid, t)
                    .normalize();
            segment.normal = segment.derivative.cross(segment.derivative.cross(new Vec3(0,1,0)).normalize()).normalize();
            return segment;
        }
    }

}
