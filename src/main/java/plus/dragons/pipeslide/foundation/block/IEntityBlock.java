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

package plus.dragons.pipeslide.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.NavigatingBE;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.block.ITE
 */
public interface IEntityBlock<T extends BlockEntity> extends EntityBlock {

    Class<T> getTileEntityClass();

    BlockEntityType<? extends T> getTileEntityType();

    default void withTileEntityDo(BlockGetter world, BlockPos pos, Consumer<T> action) {
        getTileEntityOptional(world, pos).ifPresent(action);
    }

    default InteractionResult onTileEntityUse(BlockGetter world, BlockPos pos, Function<T, InteractionResult> action) {
        return getTileEntityOptional(world, pos).map(action)
                .orElse(InteractionResult.PASS);
    }

    /**
     * if the IEntityBlock is bound to a SmartTileEntity, which implements destroy(),<br>
     * call this method in BlockBehaviour::onRemove (replace super call)
     */
    static void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState) {
        if (!blockState.hasBlockEntity())
            return;
        if (blockState.is(newBlockState.getBlock()) && newBlockState.hasBlockEntity())
            return;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof NavigatingBE navigatingBE)
            navigatingBE.destroy();
        level.removeBlockEntity(pos);
    }

    default Optional<T> getTileEntityOptional(BlockGetter world, BlockPos pos) {
        return Optional.ofNullable(getTileEntity(world, pos));
    }

    @Override
    default BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return getTileEntityType().create(pPos, pState);
    }

    @Override
    default <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level pLevel, BlockState pState,
                                                                   BlockEntityType<S> pBlockEntityType) {
        if (NavigatingBE.class.isAssignableFrom(getTileEntityClass()))
            return new NavigatingBE.Ticker<>();
        return null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    default T getTileEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        Class<T> expectedClass = getTileEntityClass();

        if (tileEntity == null)
            return null;
        if (!expectedClass.isInstance(tileEntity))
            return null;

        return (T) tileEntity;
    }

}
