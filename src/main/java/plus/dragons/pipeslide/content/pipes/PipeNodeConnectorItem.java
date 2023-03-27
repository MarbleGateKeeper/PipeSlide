package plus.dragons.pipeslide.content.pipes;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import plus.dragons.pipeslide.entry.ModBlocks;
import plus.dragons.pipeslide.entry.ModItems;
import plus.dragons.pipeslide.foundation.utility.Couple;
import plus.dragons.pipeslide.foundation.utility.Lang;

public class PipeNodeConnectorItem extends Item {
    public PipeNodeConnectorItem() {
        super(new Properties().durability(2048));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getItemInHand();
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockState state = level.getBlockState(pos);
        Player player = pContext.getPlayer();

        if (player == null)
            return super.useOn(pContext);
        if (pContext.getHand() == InteractionHand.OFF_HAND)
            return super.useOn(pContext);

        if (!isFoil(stack)) {
            if (state.getBlock() instanceof IPipeConnectableBlock pipe && pipe.hasConnectableEnd(level, pos)) {
                if (!level.isClientSide){
                    player.displayClientMessage(Lang.translateDirect("pipe.start_connect")
                            .withStyle(ChatFormatting.GREEN), true);
                    CompoundTag compoundTag = stack.getOrCreateTag();
                    compoundTag.put("Start", NbtUtils.writeBlockPos(pos));
                } else
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75f, 1);
                return InteractionResult.SUCCESS;
            }
            return super.useOn(pContext);

        } else if (player.isSteppingCarefully()) {
            if (!level.isClientSide) {
                player.displayClientMessage(Lang.translateDirect("pipe.selection_cleared").withStyle(ChatFormatting.RED), true);
                stack.setTag(null);
            } else
                level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.75f, 1);
            return InteractionResult.SUCCESS;
        }

        if(state.is(ModBlocks.PIPE_CURVE_ANCHOR.get())){
            CompoundTag compoundTag = stack.getOrCreateTag();
            if(compoundTag.contains("Mid")){
                if (!level.isClientSide){
                    player.displayClientMessage(Lang.translateDirect("pipe.anchor_chose_again")
                            .withStyle(ChatFormatting.GREEN), true);
                    compoundTag.put("Mid", NbtUtils.writeBlockPos(pos));
                }
                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide){
                    player.displayClientMessage(Lang.translateDirect("pipe.anchor_chose")
                            .withStyle(ChatFormatting.GREEN), true);
                    compoundTag.put("Mid", NbtUtils.writeBlockPos(pos));
                }
                return InteractionResult.SUCCESS;
            }
        }

        if(state.getBlock() instanceof IPipeConnectableBlock pipe){
            if(pipe.hasConnectableEnd(level, pos)){

                CompoundTag compoundTag = stack.getOrCreateTag();
                var start = NbtUtils.readBlockPos(compoundTag.getCompound("Start"));

                if(start.equals(pos)){
                    if (!level.isClientSide) {
                        player.displayClientMessage(Lang.translateDirect("pipe.selection_cleared").withStyle(ChatFormatting.RED), true);
                        stack.setTag(null);
                    } else
                        level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.75f, 1);
                    return InteractionResult.SUCCESS;
                }

                var startState = level.getBlockState(start);
                if(!(startState.getBlock() instanceof IPipeConnectableBlock pipe2) || !pipe2.hasConnectableEnd(level, pos)) {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Lang.translateDirect("pipe.start_node_not_available").withStyle(ChatFormatting.RED), true);
                        stack.setTag(null);
                    }
                    return InteractionResult.FAIL;
                }

                if(!((IPipeConnectableBlock) startState.getBlock()).canConnectTo(level,start,pos)){
                    if (!level.isClientSide) {
                        player.displayClientMessage(Lang.translateDirect("pipe.already_connected").withStyle(ChatFormatting.RED), true);
                    }
                    return InteractionResult.FAIL;
                }

                if(compoundTag.contains("Mid")){
                    var mid = NbtUtils.readBlockPos(compoundTag.getCompound("Mid"));
                    var midState = level.getBlockState(mid);

                    if(!midState.is(ModBlocks.PIPE_CURVE_ANCHOR.get())) {
                        if (!level.isClientSide) {
                            player.displayClientMessage(Lang.translateDirect("pipe.anchor_not_available").withStyle(ChatFormatting.RED), true);
                            stack.setTag(null);
                        }
                        return InteractionResult.FAIL;
                    }

                    BezierConnection bezier = new BezierConnection(Couple.create(start,pos),mid);
                    if(bezier.getLength()>32){
                        if (!level.isClientSide)
                            player.displayClientMessage(Lang.translateDirect("pipe.pipe_to_long")
                                    .withStyle(ChatFormatting.RED), true);
                        return InteractionResult.FAIL;
                    }
                    for(var seg:bezier){
                        // TODO not work as expected
                        var derivative = seg.direction;
                        if(Math.sqrt(Math.pow(Math.abs(derivative.x),2)+Math.pow(Math.abs(derivative.z),2))<Math.abs(derivative.y)){
                            if (!level.isClientSide)
                                player.displayClientMessage(Lang.translateDirect("pipe.slope_to_large")
                                        .withStyle(ChatFormatting.RED), true);
                            return InteractionResult.FAIL;
                        }
                    }

                } else {
                    var lengthSqr = start.distSqr(pos);
                    if(lengthSqr>Math.pow(32,2)){
                        if (!level.isClientSide)
                            player.displayClientMessage(Lang.translateDirect("pipe.pipe_to_long")
                                    .withStyle(ChatFormatting.RED), true);
                        return InteractionResult.FAIL;
                    }
                    var heightDiff = start.getY() - pos.getY();
                    var horizontalDistSqr = start.distSqr(new BlockPos(pos.getX(),start.getY(),pos.getZ()));
                    if(Math.pow(heightDiff,2)>horizontalDistSqr){
                        if (!level.isClientSide)
                            player.displayClientMessage(Lang.translateDirect("pipe.slope_to_large")
                                    .withStyle(ChatFormatting.RED), true);
                        return InteractionResult.FAIL;
                    }
                }

                if (level.isClientSide)
                    return InteractionResult.SUCCESS;

                BlockPos mid = compoundTag.contains("Mid")?NbtUtils.readBlockPos(compoundTag.getCompound("Mid")):null;
                Direction facing = pContext.getHorizontalDirection();
                ((IPipeConnectableBlock) startState.getBlock()).addPipeConnection(level,start,pos,facing,true,mid);
                ((IPipeConnectableBlock) state.getBlock()).addPipeConnection(level,pos,start,facing,false,mid);

                stack = player.getMainHandItem();
                if (stack.is(ModItems.PIPE_NODE_CONNECTOR.get())) {
                    stack.setTag(null);
                    player.setItemInHand(pContext.getHand(), stack);
                }

                player.displayClientMessage(Lang.translateDirect("pipe.successfully_connect")
                        .withStyle(ChatFormatting.GREEN), true);

                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide)
                    player.displayClientMessage(Lang.translateDirect("pipe.not_connectable")
                            .withStyle(ChatFormatting.RED), true);
            }
        } else {
            if (!level.isClientSide)
                player.displayClientMessage(Lang.translateDirect("pipe.not_pipe_node")
                        .withStyle(ChatFormatting.RED), true);
        }
        return InteractionResult.FAIL;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("Start");
    }
}
