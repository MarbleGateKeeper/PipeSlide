package plus.dragons.pipeslide.content.pipes.base.doubleconnect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.content.pipes.BezierConnection;
import plus.dragons.pipeslide.content.pipes.IPipeConnectableBlock;
import plus.dragons.pipeslide.content.pipes.PipeConnection;
import plus.dragons.pipeslide.foundation.block.IEntityBlock;
import plus.dragons.pipeslide.foundation.block.ProperWaterloggedBlock;
import plus.dragons.pipeslide.foundation.utility.Couple;

import java.util.ArrayList;
import java.util.List;

public abstract class PipeDoubleConnectBlock<T extends PipeDoubleConnectBlockEntity> extends Block implements ProperWaterloggedBlock, IPipeConnectableBlock<T> {
    public PipeDoubleConnectBlock() {
        this(Properties.of().mapColor(MapColor.NONE).noCollission().noOcclusion().strength(128.0f).isSuffocating(($1, $2, $3) -> false).isViewBlocking(($1, $2, $3) -> false));
    }

    public PipeDoubleConnectBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        IEntityBlock.onRemove(pState, pLevel, pPos, pNewState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hasConnectableEnd(BlockGetter world, BlockPos pos) {
        var be = getTileEntity(world, pos);
        return be.connectionA == null || be.connectionB == null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean canConnectTo(BlockGetter world, BlockPos pos, BlockPos connectTarget) {
        var be = getTileEntity(world, pos);
        if (be.connectionA != null && be.connectionA.to.equals(connectTarget))
            return false;
        else return be.connectionB == null || !be.connectionB.to.equals(connectTarget);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addPipeConnection(BlockGetter world, BlockPos pos, BlockPos connectTarget, Direction facing, boolean primaryForRender, @Nullable BlockPos midPoint) {
        var be = getTileEntity(world, pos);
        var connection = new PipeConnection(connectTarget);
        connection.primaryForRender = primaryForRender;
        if (midPoint != null) {
            connection.curveConnection = new BezierConnection(Couple.create(pos, connectTarget), midPoint);
        }
        if (be.connectionA == null) {
            be.connectionA = connection;
            be.notifyUpdate();
        } else if (be.connectionB == null) {
            be.connectionB = connection;
            be.notifyUpdate();
        } else {
            throw new RuntimeException("PipeNode " + be + " at " + pos + " cannot attach more connection");
        }
    }

    @Override
    public void removePipeConnection(BlockGetter world, BlockPos pos, BlockPos removeTarget) {
        var be = getTileEntity(world, pos);
        if (be.connectionA != null && be.connectionA.to.equals(removeTarget))
            be.connectionA = null;
        else if (be.connectionB != null && be.connectionB.to.equals(removeTarget))
            be.connectionB = null;
        be.notifyUpdate();
    }

    @Override
    public void adjustPipeConnectionShape(BlockGetter world, BlockPos pos, BlockPos connectTarget, @Nullable BlockPos midPoint) {
        var be = getTileEntity(world, pos);
        if (be.connectionA != null && be.connectionA.to.equals(connectTarget)) {
            var connection = new PipeConnection(connectTarget);
            connection.primaryForRender = be.connectionA.primaryForRender;
            if (midPoint != null) {
                connection.curveConnection = new BezierConnection(Couple.create(pos, connectTarget), midPoint);
            }
            be.connectionA = connection;
            be.notifyUpdate();
        } else if (be.connectionB != null && be.connectionB.to.equals(connectTarget)) {
            var connection = new PipeConnection(connectTarget);
            connection.primaryForRender = be.connectionB.primaryForRender;
            if (midPoint != null) {
                connection.curveConnection = new BezierConnection(Couple.create(pos, connectTarget), midPoint);
            }
            be.connectionB = connection;
            be.notifyUpdate();
        } else {
            throw new RuntimeException("PipeNode " + be + " at " + pos + " cannot replace connection of " + pos + " to " + connectTarget);
        }
    }

    @Override
    public List<PipeConnection> getConnections(BlockGetter world, BlockPos pos) {
        var be = getTileEntity(world, pos);
        var ret = new ArrayList<PipeConnection>();
        if (be.connectionA != null) ret.add(be.connectionA);
        if (be.connectionB != null) ret.add(be.connectionB);
        return ret;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pReader, @NotNull BlockPos pPos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return withWater(super.getStateForPlacement(pContext), pContext);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pDirection, @NotNull BlockState pNeighborState,
                                           @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pNeighborPos) {
        updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState pState) {
        return fluidState(pState);
    }

    @Override
    public float getShadeBrightness(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
}
