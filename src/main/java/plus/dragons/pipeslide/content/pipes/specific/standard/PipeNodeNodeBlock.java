package plus.dragons.pipeslide.content.pipes.specific.standard;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.content.pipes.BezierConnection;
import plus.dragons.pipeslide.content.pipes.IPipeConnectableNodeBlock;
import plus.dragons.pipeslide.content.pipes.PipeConnection;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.foundation.block.IEntityBlock;
import plus.dragons.pipeslide.foundation.block.ProperWaterloggedBlock;
import plus.dragons.pipeslide.foundation.utility.Couple;

public class PipeNodeNodeBlock extends Block implements IEntityBlock<PipeNodeBlockEntity>, ProperWaterloggedBlock, IPipeConnectableNodeBlock {
    public PipeNodeNodeBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(128.0f).noOcclusion());
    }

    @Override
    public Class<PipeNodeBlockEntity> getTileEntityClass() {
        return PipeNodeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PipeNodeBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_NODE.get();
    }

    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        IEntityBlock.onRemove(pState, pLevel, pPos, pNewState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hasConnectableEnd(BlockGetter world, BlockPos pos) {
        var be = getTileEntity(world,pos);
        return be.connectionA == null || be.connectionB == null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean canConnectTo(BlockGetter world, BlockPos pos, BlockPos connectTarget) {
        var be = getTileEntity(world,pos);
        if(be.connectionA!=null && be.connectionA.to.equals(connectTarget))
            return false;
        else return be.connectionB == null || !be.connectionB.to.equals(connectTarget);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addPipeConnection(BlockGetter world, BlockPos pos, BlockPos connectTarget, Direction facing, boolean primaryForRender, @Nullable BlockPos midPoint) {
        var be = getTileEntity(world,pos);
        var connection = new PipeConnection(connectTarget);
        connection.primaryForRender = primaryForRender;
        if(midPoint!=null){
            connection.curveConnection = new BezierConnection(Couple.create(pos,connectTarget),midPoint);
        }
        if(be.connectionA==null){
            be.connectionA = connection;
            be.notifyUpdate();
        } else if(be.connectionB==null) {
            be.connectionB = connection;
            be.notifyUpdate();
        } else {
            throw new RuntimeException("PipeNode " + be + " at " + pos + " cannot attack more connection");
        }
    }
}
