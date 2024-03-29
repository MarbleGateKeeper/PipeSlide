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
package plus.dragons.pipeslide.foundation.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.utility.VecHelper
 */
public class VecHelper {

    public static final Vec3 CENTER_OF_ORIGIN = new Vec3(.5, .5, .5);

    public static Vec3 rotate(Vec3 vec, Vec3 rotationVec) {
        return rotate(vec, rotationVec.x, rotationVec.y, rotationVec.z);
    }

    public static Vec3 rotate(Vec3 vec, double xRot, double yRot, double zRot) {
        return rotate(rotate(rotate(vec, xRot, Direction.Axis.X), yRot, Direction.Axis.Y), zRot, Direction.Axis.Z);
    }

    public static Vec3 rotateCentered(Vec3 vec, double deg, Direction.Axis axis) {
        Vec3 shift = getCenterOf(BlockPos.ZERO);
        return VecHelper.rotate(vec.subtract(shift), deg, axis)
                .add(shift);
    }

    public static Vec3 rotate(Vec3 vec, double deg, Direction.Axis axis) {
        if (deg == 0)
            return vec;
        if (vec == Vec3.ZERO)
            return vec;

        float angle = (float) (deg / 180f * Math.PI);
        double sin = Mth.sin(angle);
        double cos = Mth.cos(angle);
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;

        if (axis == Direction.Axis.X)
            return new Vec3(x, y * cos - z * sin, z * cos + y * sin);
        if (axis == Direction.Axis.Y)
            return new Vec3(x * cos + z * sin, y, z * cos - x * sin);
        if (axis == Direction.Axis.Z)
            return new Vec3(x * cos - y * sin, y * cos + x * sin, z);
        return vec;
    }

    public static Vec3 mirrorCentered(Vec3 vec, Mirror mirror) {
        Vec3 shift = getCenterOf(BlockPos.ZERO);
        return VecHelper.mirror(vec.subtract(shift), mirror)
                .add(shift);
    }

    public static Vec3 mirror(Vec3 vec, Mirror mirror) {
        if (mirror == null || mirror == Mirror.NONE)
            return vec;
        if (vec == Vec3.ZERO)
            return vec;

        double x = vec.x;
        double y = vec.y;
        double z = vec.z;

        if (mirror == Mirror.LEFT_RIGHT)
            return new Vec3(x, y, -z);
        if (mirror == Mirror.FRONT_BACK)
            return new Vec3(-x, y, z);
        return vec;
    }

    public static Vec3 lookAt(Vec3 vec, Vec3 fwd) {
        fwd = fwd.normalize();
        Vec3 up = new Vec3(0, 1, 0);
        double dot = fwd.dot(up);
        if (Math.abs(dot) > 1 - 1.0E-3)
            up = new Vec3(0, 0, dot > 0 ? 1 : -1);
        Vec3 right = fwd.cross(up)
                .normalize();
        up = right.cross(fwd)
                .normalize();
        double x = vec.x * right.x + vec.y * up.x + vec.z * fwd.x;
        double y = vec.x * right.y + vec.y * up.y + vec.z * fwd.y;
        double z = vec.x * right.z + vec.y * up.z + vec.z * fwd.z;
        return new Vec3(x, y, z);
    }

    public static boolean isVecPointingTowards(Vec3 vec, Direction direction) {
        return Vec3.atLowerCornerOf(direction.getNormal())
                .dot(vec.normalize()) > 0.125; // slight tolerance to activate perpendicular movement actors
    }

    public static Vec3 getCenterOf(Vec3i pos) {
        if (pos.equals(Vec3i.ZERO))
            return CENTER_OF_ORIGIN;
        return Vec3.atLowerCornerOf(pos)
                .add(.5f, .5f, .5f);
    }

    public static Vec3 offsetRandomly(Vec3 vec, RandomSource r, float radius) {
        return new Vec3(vec.x + (r.nextFloat() - .5f) * 2 * radius, vec.y + (r.nextFloat() - .5f) * 2 * radius,
                vec.z + (r.nextFloat() - .5f) * 2 * radius);
    }

    public static Vec3 axisAlingedPlaneOf(Vec3 vec) {
        vec = vec.normalize();
        return new Vec3(1, 1, 1).subtract(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
    }

    public static Vec3 axisAlingedPlaneOf(Direction face) {
        return axisAlingedPlaneOf(Vec3.atLowerCornerOf(face.getNormal()));
    }

    public static ListTag writeNBT(Vec3 vec) {
        ListTag listnbt = new ListTag();
        listnbt.add(DoubleTag.valueOf(vec.x));
        listnbt.add(DoubleTag.valueOf(vec.y));
        listnbt.add(DoubleTag.valueOf(vec.z));
        return listnbt;
    }

    public static CompoundTag writeNBTCompound(Vec3 vec) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("V", writeNBT(vec));
        return compoundTag;
    }

    public static Vec3 readNBT(ListTag list) {
        if (list.isEmpty())
            return Vec3.ZERO;
        return new Vec3(list.getDouble(0), list.getDouble(1), list.getDouble(2));
    }

    public static Vec3 readNBTCompound(CompoundTag nbt) {
        return readNBT(nbt.getList("V", Tag.TAG_DOUBLE));
    }

    public static void write(Vec3 vec, FriendlyByteBuf buffer) {
        buffer.writeDouble(vec.x);
        buffer.writeDouble(vec.y);
        buffer.writeDouble(vec.z);
    }

    public static Vec3 read(FriendlyByteBuf buffer) {
        return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static Vec3 voxelSpace(double x, double y, double z) {
        return new Vec3(x, y, z).scale(1 / 16f);
    }

    public static int getCoordinate(Vec3i pos, Direction.Axis axis) {
        return axis.choose(pos.getX(), pos.getY(), pos.getZ());
    }

    public static float getCoordinate(Vec3 vec, Direction.Axis axis) {
        return (float) axis.choose(vec.x, vec.y, vec.z);
    }

    public static boolean onSameAxis(BlockPos pos1, BlockPos pos2, Direction.Axis axis) {
        if (pos1.equals(pos2))
            return true;
        for (Direction.Axis otherAxis : Direction.Axis.values())
            if (axis != otherAxis)
                if (getCoordinate(pos1, otherAxis) != getCoordinate(pos2, otherAxis))
                    return false;
        return true;
    }

    public static Vec3 clamp(Vec3 vec, float maxLength) {
        return vec.length() > maxLength ? vec.normalize()
                .scale(maxLength) : vec;
    }

    public static Vec3 lerp(float p, Vec3 from, Vec3 to) {
        return from.add(to.subtract(from)
                .scale(p));
    }

    public static Vec3 slerp(float p, Vec3 from, Vec3 to) {
        double theta = Math.acos(from.dot(to));
        return from.scale(Mth.sin(1 - p) * theta)
                .add(to.scale(Mth.sin((float) (theta * p))))
                .scale(1 / Mth.sin((float) theta));
    }

    public static Vec3 clampComponentWise(Vec3 vec, float maxLength) {
        return new Vec3(Mth.clamp(vec.x, -maxLength, maxLength), Mth.clamp(vec.y, -maxLength, maxLength),
                Mth.clamp(vec.z, -maxLength, maxLength));
    }

    public static Vec3 project(Vec3 vec, Vec3 ontoVec) {
        if (ontoVec.equals(Vec3.ZERO))
            return Vec3.ZERO;
        return ontoVec.scale(vec.dot(ontoVec) / ontoVec.lengthSqr());
    }

    @Nullable
    public static Vec3 intersectSphere(Vec3 origin, Vec3 lineDirection, Vec3 sphereCenter, double radius) {
        if (lineDirection.equals(Vec3.ZERO))
            return null;
        if (lineDirection.length() != 1)
            lineDirection = lineDirection.normalize();

        Vec3 diff = origin.subtract(sphereCenter);
        double lineDotDiff = lineDirection.dot(diff);
        double delta = lineDotDiff * lineDotDiff - (diff.lengthSqr() - radius * radius);
        if (delta < 0)
            return null;
        double t = -lineDotDiff + Math.sqrt(delta);
        return origin.add(lineDirection.scale(t));
    }

    public static Vec3 bezier(Vec3 end1, Vec3 end2, Vec3 mid, float t) {
        Vec3 inner1 = lerp(t, end1, mid);
        Vec3 inner2 = lerp(t, mid, end2);
        return lerp(t, inner1, inner2);
    }

    /*public static Vec3 bezierDerivative(Vec3 end1, Vec3 end2, Vec3 mid, float t) {
        return end1.scale(2 * t - 2)
                .add(mid.scale(- 4 * t + 2))
                .add(end2.scale(2 * t));
    }

    public static Vec3 bezier(Vec3 end1, Vec3 end2, Vec3 mid1, Vec3 mid2, float t) {
        Vec3 v1 = lerp(t, end1, mid1);
        Vec3 v2 = lerp(t, mid1, mid2);
        Vec3 v3 = lerp(t, mid2, end2);
        Vec3 inner1 = lerp(t, v1, v2);
        Vec3 inner2 = lerp(t, v2, v3);
        Vec3 result = lerp(t, inner1, inner2);
        return result;
    }

    public static Vec3 bezierDerivative(Vec3 end1, Vec3 end2, Vec3 mid1, Vec3 mid2, float t) {
        return end1.scale(-3 * t * t + 6 * t - 3)
                .add(mid1.scale(9 * t * t - 12 * t + 3))
                .add(mid2.scale(-9 * t * t + 6 * t))
                .add(end2.scale(3 * t * t));
    }*/

}