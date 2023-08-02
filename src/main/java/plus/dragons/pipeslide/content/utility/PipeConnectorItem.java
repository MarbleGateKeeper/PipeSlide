package plus.dragons.pipeslide.content.utility;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import plus.dragons.pipeslide.content.pipes.BezierConnection;
import plus.dragons.pipeslide.content.pipes.IPipeConnectableBlock;
import plus.dragons.pipeslide.entry.ModBlocks;
import plus.dragons.pipeslide.entry.ModItems;
import plus.dragons.pipeslide.foundation.utility.Couple;
import plus.dragons.pipeslide.foundation.utility.Lang;

import javax.annotation.Nullable;
import java.util.List;

public class PipeConnectorItem extends Item {
    public PipeConnectorItem() {
        super(new Properties().durability(128));
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

        CompoundTag compoundTag = stack.getOrCreateTag();
        if (!compoundTag.contains("Start")) {
            if (state.getBlock() instanceof IPipeConnectableBlock<?> pipe && pipe.hasConnectableEnd(level, pos)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.start_connect")
                            .withStyle(ChatFormatting.GREEN), true);
                    compoundTag.put("Start", NbtUtils.writeBlockPos(pos));
                } else
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75f, 1);
                return InteractionResult.SUCCESS;
            }
            return super.useOn(pContext);

        } else if (player.isSteppingCarefully()) {
            if (!level.isClientSide) {
                player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.selection_cleared").withStyle(ChatFormatting.RED), true);
                compoundTag.remove("Start");
                compoundTag.remove("Mid");
            } else
                level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.75f, 1);
            return InteractionResult.SUCCESS;
        }

        if (state.is(ModBlocks.PIPE_CURVE_ANCHOR.get())) {
            if (compoundTag.contains("Mid")) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.anchor_re_selected")
                            .withStyle(ChatFormatting.GREEN), true);
                    compoundTag.put("Mid", NbtUtils.writeBlockPos(pos));
                }
                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.anchor_selected")
                            .withStyle(ChatFormatting.GREEN), true);
                    compoundTag.put("Mid", NbtUtils.writeBlockPos(pos));
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (state.getBlock() instanceof IPipeConnectableBlock<?> pipe) {
            if (pipe.hasConnectableEnd(level, pos)) {
                var start = NbtUtils.readBlockPos(compoundTag.getCompound("Start"));
                if (start.equals(pos)) {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.selection_cleared").withStyle(ChatFormatting.RED), true);
                        stack.setTag(null);
                    } else
                        level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.75f, 1);
                    return InteractionResult.SUCCESS;
                }

                var startState = level.getBlockState(start);
                if (!(startState.getBlock() instanceof IPipeConnectableBlock<?> pipe2) || !pipe2.hasConnectableEnd(level, start)) {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.start_node_not_available").withStyle(ChatFormatting.RED), true);
                        stack.setTag(null);
                    }
                    return InteractionResult.FAIL;
                }

                if (!((IPipeConnectableBlock<?>) startState.getBlock()).canConnectTo(level, start, pos)) {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.already_connected").withStyle(ChatFormatting.RED), true);
                    }
                    return InteractionResult.FAIL;
                }

                if (compoundTag.contains("Mid")) {
                    var mid = NbtUtils.readBlockPos(compoundTag.getCompound("Mid"));
                    var midState = level.getBlockState(mid);

                    if (!midState.is(ModBlocks.PIPE_CURVE_ANCHOR.get())) {
                        if (!level.isClientSide) {
                            player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.anchor_not_available").withStyle(ChatFormatting.RED), true);
                            stack.setTag(null);
                        }
                        return InteractionResult.FAIL;
                    }

                    BezierConnection bezier = new BezierConnection(Couple.create(start, pos), mid);
                    if (bezier.getLength() > 64) {
                        if (!level.isClientSide)
                            player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.pipe_too_long")
                                    .withStyle(ChatFormatting.RED), true);
                        return InteractionResult.FAIL;
                    }

                } else {
                    var lengthSqr = start.distSqr(pos);
                    if (lengthSqr > Math.pow(64, 2)) {
                        if (!level.isClientSide)
                            player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.pipe_too_long")
                                    .withStyle(ChatFormatting.RED), true);
                        return InteractionResult.FAIL;
                    }
                }

                if (level.isClientSide)
                    return InteractionResult.SUCCESS;

                BlockPos mid = compoundTag.contains("Mid") ? NbtUtils.readBlockPos(compoundTag.getCompound("Mid")) : null;
                Direction facing = pContext.getHorizontalDirection();
                ((IPipeConnectableBlock<?>) startState.getBlock()).addPipeConnection(level, start, pos, facing, true, mid);
                ((IPipeConnectableBlock<?>) state.getBlock()).addPipeConnection(level, pos, start, facing, false, mid);

                stack = player.getMainHandItem();
                if (stack.is(ModItems.PIPE_CONNECTOR.get())) {
                    stack.setTag(null);
                    player.setItemInHand(pContext.getHand(), stack);
                }

                stack.hurtAndBreak(1, player, (context) -> {
                    context.broadcastBreakEvent(pContext.getHand());
                });
                player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.successfully_connect")
                        .withStyle(ChatFormatting.GREEN), true);

                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide)
                    player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.not_connectable")
                            .withStyle(ChatFormatting.RED), true);
            }
        } else {
            if (!level.isClientSide)
                player.displayClientMessage(Lang.translateDirect("pipe_connector.notify.not_pipe_node")
                        .withStyle(ChatFormatting.RED), true);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Lang.translateDirect("tooltips.how_to_use").withStyle(Style.EMPTY.withBold(true)));
        pTooltipComponents.add(Lang.translateDirect("tooltips.pipe_connector"));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("Start");
    }
}
