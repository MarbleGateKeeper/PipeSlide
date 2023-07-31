package plus.dragons.pipeslide.content.pipes.specific.base.doubleconnect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.content.pipes.BezierConnection;
import plus.dragons.pipeslide.content.pipes.IPipeConnectableBlock;
import plus.dragons.pipeslide.content.pipes.PipeConnection;
import plus.dragons.pipeslide.foundation.block.IEntityBlock;
import plus.dragons.pipeslide.foundation.block.ProperWaterloggedBlock;
import plus.dragons.pipeslide.foundation.utility.Couple;

public abstract class PipeDoubleConnectBlock<T extends PipeDoubleConnectBlockEntity> extends Block implements ProperWaterloggedBlock, IPipeConnectableBlock<T> {
    public PipeDoubleConnectBlock() {
        super(Properties.of().mapColor(MapColor.NONE).noCollission().noOcclusion().strength(128.0f).isSuffocating(($1, $2, $3)->false).isViewBlocking(($1, $2, $3)->false));
    }

    public PipeDoubleConnectBlock(Properties pProperties) {
        super(pProperties);
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
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pReader, @NotNull BlockPos pPos) {
        return true;
    }

    @Override
    public float getShadeBrightness(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return 1.0F;
    }
}
