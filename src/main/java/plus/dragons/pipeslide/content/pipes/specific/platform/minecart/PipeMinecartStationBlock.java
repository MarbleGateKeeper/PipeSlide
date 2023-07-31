package plus.dragons.pipeslide.content.pipes.specific.platform.minecart;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import plus.dragons.pipeslide.content.pipes.base.doubleconnect.PipeDoubleConnectBlock;
import plus.dragons.pipeslide.entry.ModBlockEntities;
import plus.dragons.pipeslide.foundation.mixin.BoatItemAccessor;
import plus.dragons.pipeslide.foundation.mixin.MinecartItemAccessor;

public class PipeMinecartStationBlock extends PipeDoubleConnectBlock<PipeMinecartStationBlockEntity> {
    public PipeMinecartStationBlock() {
        super();
    }

    @Override
    public Class<PipeMinecartStationBlockEntity> getTileEntityClass() {
        return PipeMinecartStationBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipeMinecartStationBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_MINECART_STATION.get();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }

    @Override
    @NotNull
    public InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(pHit.getDirection()== Direction.UP && pPlayer.getItemInHand(pHand).getItem() instanceof MinecartItem minecartItem){
            if(!pLevel.isClientSide()) {
                var itemstack = pPlayer.getItemInHand(pHand);
                AbstractMinecart abstractminecart = AbstractMinecart.createMinecart(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.0625D, (double)pPos.getZ() + 0.5D,
                        ((MinecartItemAccessor) minecartItem).getType());
                if (itemstack.hasCustomHoverName()) {
                    abstractminecart.setCustomName(itemstack.getHoverName());
                }
                pLevel.addFreshEntity(abstractminecart);
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
        return InteractionResult.PASS;
    }
}
