package plus.dragons.pipeslide.content.pipes.specific.platform.boat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
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

public class PipeBoatDockBlock extends PipeDoubleConnectBlock<PipeBoatDockBlockEntity> {
    public PipeBoatDockBlock() {
        super();
    }

    @Override
    public Class<PipeBoatDockBlockEntity> getTileEntityClass() {
        return PipeBoatDockBlockEntity.class;
    }

    @Override
    public BlockEntityType<PipeBoatDockBlockEntity> getTileEntityType() {
        return ModBlockEntities.PIPE_BOAT_DOCK.get();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }

    @Override
    @NotNull
    public InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(pHit.getDirection()== Direction.UP && pPlayer.getItemInHand(pHand).getItem() instanceof BoatItem boatItem){
            if(!pLevel.isClientSide()) {
                var boatItemStack = pPlayer.getItemInHand(pHand);
                Boat boat = ((BoatItemAccessor)boatItem).invokeGetBoat(pLevel, pHit);
                boat.setVariant(((BoatItemAccessor)boatItem).getType());
                pLevel.addFreshEntity(boat);
                pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, pHit.getLocation());
                if (!pPlayer.getAbilities().instabuild) {
                    boatItemStack.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
        return InteractionResult.PASS;
    }
}
