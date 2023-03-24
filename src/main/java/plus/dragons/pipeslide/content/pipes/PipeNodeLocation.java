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
package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import plus.dragons.pipeslide.content.pipes.node.BezierConnection;
import plus.dragons.pipeslide.foundation.utility.Iterate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.content.logistics.trains.TrackNodeLocation
 */
public class PipeNodeLocation extends Vec3i {

    public PipeNodeLocation(Vec3 vec) {
        this(vec.x, vec.y, vec.z);
    }

    public PipeNodeLocation(double pX, double pY, double pZ) {
        super((int) Math.round(pX * 2), (int) Math.floor(pY * 2), (int) Math.round(pZ * 2));
    }

    private static PipeNodeLocation fromPackedPos(BlockPos bufferPos) {
        return new PipeNodeLocation(bufferPos);
    }

    private PipeNodeLocation(BlockPos readBlockPos) {
        super(readBlockPos.getX(), readBlockPos.getY(), readBlockPos.getZ());
    }

    public Vec3 getLocation() {
        return new Vec3(getX() / 2.0, getY() / 2.0, getZ() / 2.0);
    }

    @Override
    public boolean equals(Object pOther) {
        return equalsIgnoreDim(pOther) && pOther instanceof PipeNodeLocation;
    }

    public boolean equalsIgnoreDim(Object pOther) {
        return super.equals(pOther);
    }

    @Override
    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public CompoundTag write() {
        return NbtUtils.writeBlockPos(new BlockPos(this));
    }

    public static PipeNodeLocation read(CompoundTag tag) {
        return fromPackedPos(NbtUtils.readBlockPos(tag));
    }

    public void send(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.getX());
        buffer.writeShort(this.getY());
        buffer.writeVarInt(this.getZ());
    }

    public static PipeNodeLocation receive(FriendlyByteBuf buffer) {
        PipeNodeLocation location = fromPackedPos(new BlockPos(
                buffer.readVarInt(),
                buffer.readShort(),
                buffer.readVarInt()
        ));
        return location;
    }

    public Collection<BlockPos> allAdjacent() {
        Set<BlockPos> set = new HashSet<>();
        Vec3 vec3 = getLocation();
        double step = 1 / 8f;
        for (int x : Iterate.positiveAndNegative)
            for (int y : Iterate.positiveAndNegative)
                for (int z : Iterate.positiveAndNegative)
                    set.add(BlockPos.containing(vec3.add(x * step, y * step, z * step)));
        return set;
    }

    public static class DiscoveredLocation extends PipeNodeLocation {

        BezierConnection turn = null;
        boolean forceNode = false;
        Vec3 direction;
        Vec3 normal;

        public DiscoveredLocation(Level level, double pX, double pY, double pZ) {
            super(pX, pY, pZ);
        }

        public DiscoveredLocation(ResourceKey<Level> dimension, Vec3 vec) {
            super(vec);
        }

        public DiscoveredLocation(Level level, Vec3 vec) {
            this(level.dimension(), vec);
        }

        public DiscoveredLocation viaTurn(BezierConnection turn) {
            this.turn = turn;
            if (turn != null)
                forceNode();
            return this;
        }

        public DiscoveredLocation forceNode() {
            forceNode = true;
            return this;
        }

        public DiscoveredLocation withNormal(Vec3 normal) {
            this.normal = normal;
            return this;
        }

        public DiscoveredLocation withDirection(Vec3 direction) {
            this.direction = direction == null ? null : direction.normalize();
            return this;
        }

        public boolean connectedViaTurn() {
            return turn != null;
        }

        public BezierConnection getTurn() {
            return turn;
        }

        public boolean shouldForceNode() {
            return forceNode;
        }

        public boolean notInLineWith(Vec3 direction) {
            return this.direction != null
                    && Math.max(direction.dot(this.direction), direction.dot(this.direction.scale(-1))) < 7 / 8f;
        }

    }

}