package plus.dragons.pipeslide.content.pipes;

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
    private int segments;

    private AABB bounds;

    public BezierConnection(Couple<BlockPos> endPoints, BlockPos midPoint) {
        this.endPoints = endPoints;
        this.midPoint = midPoint;
        this.resolved = false;
    }

    public BezierConnection(CompoundTag compound) {
        this(Couple.deserializeEach(compound.getList("EndPoints", Tag.TAG_COMPOUND), NbtUtils::readBlockPos),
                NbtUtils.readBlockPos(compound.getCompound("MidPoint")));
    }

    public CompoundTag write() {
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

    // Runtime information
    public double getLength() {
        resolve();
        return length;
    }

    public int getSegmentCount() {
        resolve();
        return segments;
    }

    public Vec3 getPosition(double t) {
        resolve();
        return VecHelper.bezier(VecHelper.getCenterOf(endPoints.getFirst()), VecHelper.getCenterOf(endPoints.getSecond()), VecHelper.getCenterOf(midPoint), (float) t);
    }


    public AABB getBounds() {
        resolve();
        return bounds;
    }

    public Vec3 getDirection(double t) {
        resolve();
        Vec3 end1 = VecHelper.getCenterOf(endPoints.getFirst());
        Vec3 end2 = VecHelper.getCenterOf(endPoints.getSecond());
        Vec3 mid = VecHelper.getCenterOf(midPoint);

        if(t==1){
            return VecHelper.bezier(end1,end2,mid,1).subtract(VecHelper.bezier(end1,end2,mid,segments-1/(float)segments)).normalize();
        } else {
            int index = Math.min(segments-1,(int)Math.floor(t/(1F/segments)));
            return VecHelper.bezier(end1,end2,mid,(index + 1)/(float)segments).subtract(VecHelper.bezier(end1,end2,mid,index/(float)segments)).normalize();
        }
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

        segments = (int) (length * 3);

        bounds = new AABB(end1, end2);

        // determine step lut
        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            Vec3 result = VecHelper.bezier(end1, end2, mid, t);
            bounds = bounds.minmax(new AABB(result, result));
        }

        bounds = bounds.inflate(1.375f);
    }

    @Override
    public Iterator<Segment> iterator() {
        resolve();
        return new Bezierator(this);
    }


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
        public Vec3 start;
        public Vec3 end;
        public Vec3 direction;
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
            return segment.index + 1 < bc.segments;
        }

        @Override
        public Segment next() {
            segment.index++;
            float t = segment.index;
            segment.start = VecHelper.bezier(end1, end2, mid, segment.index/(float) bc.getSegmentCount());
            segment.end = VecHelper.bezier(end1, end2, mid, (segment.index + 1)/(float) bc.getSegmentCount());
            segment.direction = VecHelper.bezier(end1, end2, mid, t).subtract(VecHelper.bezier(end1, end2, mid, (segment.index + 1)/(float) bc.getSegmentCount()))
                    .normalize();
            return segment;
        }
    }

    private SegmentRenderData[] bakedSegments;

    public static class SegmentRenderData {

        public Vec3 start;
        public Vec3 end;
        public Vec3 direction;

    }

    public SegmentRenderData[] getSegmentRenderData() {

        if (bakedSegments != null)
            return bakedSegments;

        int segmentCount = getSegmentCount();
        bakedSegments = new SegmentRenderData[segmentCount + 1];

        for (BezierConnection.Segment segment : this) {
            int i = segment.index;

            SegmentRenderData ends = bakedSegments[i] = new SegmentRenderData();

            ends.start = segment.start;
            ends.end = segment.end;
            ends.direction = segment.direction;
        }

        return bakedSegments;
    }

}
