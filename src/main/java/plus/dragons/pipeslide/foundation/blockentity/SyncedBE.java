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
package plus.dragons.pipeslide.foundation.blockentity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.tileEntity.SyncedTileEntity
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class SyncedBE extends BlockEntity {

    public SyncedBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // Synchronizing on LevelChunk Load
    @Override
    public CompoundTag getUpdateTag() {
        return writeClient(new CompoundTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        readClient(tag);
    }

    // Synchronizing on Block Update
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this); // Call getUpdateTag()
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();
        readClient(tag == null ? new CompoundTag() : tag);
    }

    // Special handling for client update packets
    public void readClient(CompoundTag tag) {
        load(tag);
    }

    // Special handling for client update packets
    public CompoundTag writeClient(CompoundTag tag) {
        saveAdditional(tag);
        return tag;
    }

    public void sendData() {
        if (level != null && !level.isClientSide)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2 | 4 | 16);
    }

    public void causeBlockUpdate() {
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 1);
    }

    public void notifyUpdate() {
        setChanged();
        sendData();
    }

    public PacketDistributor.PacketTarget packetTarget() {
        return PacketDistributor.TRACKING_CHUNK.with(this::containedChunk);
    }

    public LevelChunk containedChunk() {
        return level.getChunkAt(worldPosition);
    }
}
