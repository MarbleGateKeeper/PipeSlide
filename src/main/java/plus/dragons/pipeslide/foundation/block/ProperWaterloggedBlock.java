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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.block.ProperWaterloggedBlock
 * <p>
 * Waterlog checklist: <br>
 * 1. createBlockStateDefinition -> add WATERLOGGED <br>
 * 2. constructor -> default WATERLOGGED to false <br>
 * 3. getFluidState -> return fluidState <br>
 * 4. getStateForPlacement -> call withWater <br>
 * 5. updateShape -> call updateWater
 */
public interface ProperWaterloggedBlock extends SimpleWaterloggedBlock {

    BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    default FluidState fluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    default void updateWater(LevelAccessor level, BlockState state, BlockPos pos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    default BlockState withWater(BlockState placementState, BlockPlaceContext ctx) {
        return withWater(ctx.getLevel(), placementState, ctx.getClickedPos());
    }

    static BlockState withWater(LevelAccessor level, BlockState placementState, BlockPos pos) {
        if (placementState == null)
            return null;
        if (!(placementState.getBlock() instanceof SimpleWaterloggedBlock))
            return placementState;
        FluidState ifluidstate = level.getFluidState(pos);
        return placementState.setValue(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
    }

}