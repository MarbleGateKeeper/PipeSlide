package plus.dragons.pipeslide.content.pipes.specific.platform.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import plus.dragons.pipeslide.content.pipes.specific.platform.PipePlatformBlockEntity;
import plus.dragons.pipeslide.entry.ModBlockEntities;

public class PipeItemReceiverBlockEntity extends PipePlatformBlockEntity<ItemEntity> {

    public PipeItemReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_ITEM_RECEIVER.get(), pos, state);
    }

    @Override
    protected Class<ItemEntity> getPassengerClass() {
        return ItemEntity.class;
    }
}
