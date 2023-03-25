package plus.dragons.pipeslide.content.pipes.node;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.foundation.block.IEntityBlock;
import plus.dragons.pipeslide.foundation.block.ProperWaterloggedBlock;

public class PipeNodeBlock extends Block implements IEntityBlock<PipeNodeBlockEntity>, ProperWaterloggedBlock {
    public PipeNodeBlock() {
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
}
