/*
MIT License

Copyright (c) 2019 simibubi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package plus.dragons.pipeslide.content.pipes.node;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.foundation.utility.Couple;
import plus.dragons.pipeslide.foundation.utility.VecHelper;

import java.util.Iterator;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.content.logistics.trains.BezierConnection
 */
public class BezierConnection implements Iterable<BezierConnection.Segment> {

    public Couple<BlockPos> tePositions;
    public Couple<Vec3> starts;
    public Couple<Vec3> axes;
    public Couple<Vec3> normals;
    public boolean primary;
    public boolean hasGirder;

    // runtime

    Vec3 finish1;
    Vec3 finish2;
    private boolean resolved;
    private double length;
    private float[] stepLUT;
    private int segments;

    private double radius;
    private double handleLength;

    private AABB bounds;

    public BezierConnection(Couple<BlockPos> positions, Couple<Vec3> starts, Couple<Vec3> axes, Couple<Vec3> normals,
                            boolean primary, boolean girder) {
        tePositions = positions;
        this.starts = starts;
        this.axes = axes;
        this.normals = normals;
        this.primary = primary;
        this.hasGirder = girder;
        resolved = false;
    }

    public BezierConnection secondary() {
        return new BezierConnection(tePositions.swap(), starts.swap(), axes.swap(), normals.swap(), !primary,
                hasGirder);
    }

    public BezierConnection(CompoundTag compound, BlockPos localTo) {
        this(Couple.deserializeEach(compound.getList("Positions", Tag.TAG_COMPOUND), NbtUtils::readBlockPos)
                        .map(b -> b.offset(localTo)),
                Couple.deserializeEach(compound.getList("Starts", Tag.TAG_COMPOUND), VecHelper::readNBTCompound)
                        .map(v -> v.add(Vec3.atLowerCornerOf(localTo))),
                Couple.deserializeEach(compound.getList("Axes", Tag.TAG_COMPOUND), VecHelper::readNBTCompound),
                Couple.deserializeEach(compound.getList("Normals", Tag.TAG_COMPOUND), VecHelper::readNBTCompound),
                compound.getBoolean("Primary"), compound.getBoolean("Girder"));
    }

    public CompoundTag write(BlockPos localTo) {
        Couple<BlockPos> tePositions = this.tePositions.map(b -> b.subtract(localTo));
        Couple<Vec3> starts = this.starts.map(v -> v.subtract(Vec3.atLowerCornerOf(localTo)));

        CompoundTag compound = new CompoundTag();
        compound.putBoolean("Girder", hasGirder);
        compound.putBoolean("Primary", primary);
        compound.put("Positions", tePositions.serializeEach(NbtUtils::writeBlockPos));
        compound.put("Starts", starts.serializeEach(VecHelper::writeNBTCompound));
        compound.put("Axes", axes.serializeEach(VecHelper::writeNBTCompound));
        compound.put("Normals", normals.serializeEach(VecHelper::writeNBTCompound));
        return compound;
    }

    public BezierConnection(FriendlyByteBuf buffer) {
        this(Couple.create(buffer::readBlockPos), Couple.create(() -> VecHelper.read(buffer)),
                Couple.create(() -> VecHelper.read(buffer)), Couple.create(() -> VecHelper.read(buffer)),
                buffer.readBoolean(), buffer.readBoolean());
    }

    public void write(FriendlyByteBuf buffer) {
        tePositions.forEach(buffer::writeBlockPos);
        starts.forEach(v -> VecHelper.write(v, buffer));
        axes.forEach(v -> VecHelper.write(v, buffer));
        normals.forEach(v -> VecHelper.write(v, buffer));
        buffer.writeBoolean(primary);
        buffer.writeBoolean(hasGirder);
    }

    public BlockPos getKey() {
        return tePositions.getSecond();
    }

    public boolean isPrimary() {
        return primary;
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
        return VecHelper.bezier(starts.getFirst(), starts.getSecond(), finish1, finish2, (float) t);
    }

    public double getRadius() {
        resolve();
        return radius;
    }

    public double getHandleLength() {
        resolve();
        return handleLength;
    }

    public float getSegmentT(int index) {
        return index == segments ? 1 : index * stepLUT[index] / segments;
    }

    public double incrementT(double currentT, double distance) {
        resolve();
        double dx =
                VecHelper.bezierDerivative(starts.getFirst(), starts.getSecond(), finish1, finish2, (float) currentT)
                        .length() / getLength();
        return currentT + distance / dx;

    }

    public AABB getBounds() {
        resolve();
        return bounds;
    }

    public Vec3 getNormal(double t) {
        resolve();
        Vec3 end1 = starts.getFirst();
        Vec3 end2 = starts.getSecond();
        Vec3 fn1 = normals.getFirst();
        Vec3 fn2 = normals.getSecond();

        Vec3 derivative = VecHelper.bezierDerivative(end1, end2, finish1, finish2, (float) t)
                .normalize();
        Vec3 faceNormal = fn1.equals(fn2) ? fn1 : VecHelper.slerp((float) t, fn1, fn2);
        Vec3 normal = faceNormal.cross(derivative)
                .normalize();
        return derivative.cross(normal);
    }

    private void resolve() {
        if (resolved)
            return;
        resolved = true;

        Vec3 end1 = starts.getFirst();
        Vec3 end2 = starts.getSecond();
        Vec3 axis1 = axes.getFirst()
                .normalize();
        Vec3 axis2 = axes.getSecond()
                .normalize();

        determineHandles(end1, end2, axis1, axis2);

        finish1 = axis1.scale(handleLength)
                .add(end1);
        finish2 = axis2.scale(handleLength)
                .add(end2);

        int scanCount = 16;
        length = 0;

        {
            Vec3 previous = end1;
            for (int i = 0; i <= scanCount; i++) {
                float t = i / (float) scanCount;
                Vec3 result = VecHelper.bezier(end1, end2, finish1, finish2, t);
                if (previous != null)
                    length += result.distanceTo(previous);
                previous = result;
            }
        }

        segments = (int) (length * 2);
        stepLUT = new float[segments + 1];
        stepLUT[0] = 1;
        float combinedDistance = 0;

        bounds = new AABB(end1, end2);

        // determine step lut
        {
            Vec3 previous = end1;
            for (int i = 0; i <= segments; i++) {
                float t = i / (float) segments;
                Vec3 result = VecHelper.bezier(end1, end2, finish1, finish2, t);
                bounds = bounds.minmax(new AABB(result, result));
                if (i > 0) {
                    combinedDistance += result.distanceTo(previous) / length;
                    stepLUT[i] = (float) (t / combinedDistance);
                }
                previous = result;
            }
        }

        bounds = bounds.inflate(1.375f);
    }

    private void determineHandles(Vec3 end1, Vec3 end2, Vec3 axis1, Vec3 axis2) {
        Vec3 cross1 = axis1.cross(new Vec3(0, 1, 0));
        Vec3 cross2 = axis2.cross(new Vec3(0, 1, 0));

        radius = 0;
        double a1 = Mth.atan2(-axis2.z, -axis2.x);
        double a2 = Mth.atan2(axis1.z, axis1.x);
        double angle = a1 - a2;

        float circle = 2 * Mth.PI;
        angle = (angle + circle) % circle;
        if (Math.abs(circle - angle) < Math.abs(angle))
            angle = circle - angle;

        if (Mth.equal(angle, 0)) {
            double[] intersect = VecHelper.intersect(end1, end2, axis1, cross2, Direction.Axis.Y);
            if (intersect != null) {
                double t = Math.abs(intersect[0]);
                double u = Math.abs(intersect[1]);
                double min = Math.min(t, u);
                double max = Math.max(t, u);

                if (min > 1.2 && max / min > 1 && max / min < 3) {
                    handleLength = (max - min);
                    return;
                }
            }

            handleLength = end2.distanceTo(end1) / 3;
            return;
        }

        double n = circle / angle;
        double factor = 4 / 3d * Math.tan(Math.PI / (2 * n));
        double[] intersect = VecHelper.intersect(end1, end2, cross1, cross2, Direction.Axis.Y);

        if (intersect == null) {
            handleLength = end2.distanceTo(end1) / 3;
            return;
        }

        radius = Math.abs(intersect[1]);
        handleLength = radius * factor;
        if (Mth.equal(handleLength, 0))
            handleLength = 1;
    }

    @Override
    public Iterator<Segment> iterator() {
        resolve();
        var offset = Vec3.atLowerCornerOf(tePositions.getFirst())
                .scale(-1)
                .add(0, 3 / 16f, 0);
        return new Bezierator(this, offset);
    }

    public int getTrackItemCost() {
        return (getSegmentCount() + 1) / 2;
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
        public Vec3 position;
        public Vec3 derivative;
        public Vec3 faceNormal;
        public Vec3 normal;

    }

    private static class Bezierator implements Iterator<Segment> {

        private final BezierConnection bc;
        private final Segment segment;
        private final Vec3 end1;
        private final Vec3 end2;
        private final Vec3 finish1;
        private final Vec3 finish2;
        private final Vec3 faceNormal1;
        private final Vec3 faceNormal2;

        private Bezierator(BezierConnection bc, Vec3 offset) {
            bc.resolve();
            this.bc = bc;

            end1 = bc.starts.getFirst()
                    .add(offset);
            end2 = bc.starts.getSecond()
                    .add(offset);

            finish1 = bc.axes.getFirst()
                    .scale(bc.handleLength)
                    .add(end1);
            finish2 = bc.axes.getSecond()
                    .scale(bc.handleLength)
                    .add(end2);

            faceNormal1 = bc.normals.getFirst();
            faceNormal2 = bc.normals.getSecond();
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
            segment.position = VecHelper.bezier(end1, end2, finish1, finish2, t);
            segment.derivative = VecHelper.bezierDerivative(end1, end2, finish1, finish2, t)
                    .normalize();
            segment.faceNormal =
                    faceNormal1.equals(faceNormal2) ? faceNormal1 : VecHelper.slerp(t, faceNormal1, faceNormal2);
            segment.normal = segment.faceNormal.cross(segment.derivative)
                    .normalize();
            return segment;
        }
    }

}
