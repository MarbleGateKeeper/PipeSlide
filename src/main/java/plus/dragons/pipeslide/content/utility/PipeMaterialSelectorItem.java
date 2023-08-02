package plus.dragons.pipeslide.content.utility;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import plus.dragons.pipeslide.content.pipes.IPipeConnectableBlock;
import plus.dragons.pipeslide.foundation.utility.Lang;

import javax.annotation.Nullable;
import java.util.List;

public class PipeMaterialSelectorItem extends Item {
    public PipeMaterialSelectorItem() {
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
            if (state.getBlock() instanceof IPipeConnectableBlock<?> pipe) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("pipe_material_selector.notify.first_node_selected")
                            .withStyle(ChatFormatting.GREEN), true);
                    compoundTag.put("Start", NbtUtils.writeBlockPos(pos));
                } else
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75f, 1);
                return InteractionResult.SUCCESS;
            }
            return super.useOn(pContext);
        }

        if (player.isSteppingCarefully()) {
            if (!level.isClientSide) {
                player.displayClientMessage(Lang.translateDirect("pipe_material_selector.notify.selection_cleared").withStyle(ChatFormatting.RED), true);
                compoundTag.remove("Start");
                compoundTag.remove("End");
            } else
                level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.75f, 1);
            return InteractionResult.SUCCESS;
        }

        var start = NbtUtils.readBlockPos(compoundTag.getCompound("Start"));
        if (state.getBlock() instanceof IPipeConnectableBlock<?>) {
            if (hasConnectionBetween(level, start, pos)) {
                var hasEnd = compoundTag.contains("End");
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect(hasEnd ? "pipe_material_selector.notify.pipe_re_selected" : "pipe_adjustor.notify.pipe_selected")
                            .withStyle(ChatFormatting.GREEN), true);
                    compoundTag.put("End", NbtUtils.writeBlockPos(pos));
                } else
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75f, 1);
                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("pipe_material_selector.notify.no_valid_pipe_between_these_nodes").withStyle(ChatFormatting.RED), true);
                    stack.setTag(null);
                }
                return InteractionResult.FAIL;
            }
        }

        if (compoundTag.contains("End")) {
            if (!(level.getBlockState(start).getBlock() instanceof IPipeConnectableBlock<?> startPipe)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("pipe_material_selector.notify.start_node_not_available").withStyle(ChatFormatting.RED), true);
                    stack.setTag(null);
                }
                return InteractionResult.FAIL;
            }
            var end = NbtUtils.readBlockPos(compoundTag.getCompound("End"));
            if (!(level.getBlockState(end).getBlock() instanceof IPipeConnectableBlock<?> endPipe)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("pipe_material_selector.notify.end_node_not_available").withStyle(ChatFormatting.RED), true);
                    stack.setTag(null);
                }
                return InteractionResult.FAIL;
            }
            if (!hasConnectionBetween(level, start, end)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Lang.translateDirect("pipe_material_selector.notify.no_valid_pipe_between_selected_nodes").withStyle(ChatFormatting.RED), true);
                    stack.setTag(null);
                }
                return InteractionResult.FAIL;
            }

            // TODO change texture on clicked block

            if (level.isClientSide)
                return InteractionResult.SUCCESS;
            stack.hurtAndBreak(1, player, (context) -> {
                context.broadcastBreakEvent(pContext.getHand());
            });
            player.displayClientMessage(Lang.translateDirect("pipe_material_selector.notify.successfully_adjust")
                    .withStyle(ChatFormatting.GREEN), true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean hasConnectionBetween(Level level, BlockPos pos1, BlockPos pos2) {
        if (level.getBlockState(pos1).getBlock() instanceof IPipeConnectableBlock<?> pipe) {
            for (var connection : pipe.getConnections(level, pos1)) {
                if (connection.to.equals(pos2)) return true;
            }
        }
        return false;
    }


    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Lang.translateDirect("tooltips.how_to_use").withStyle(Style.EMPTY.withBold(true)));
        pTooltipComponents.add(Lang.translateDirect("tooltips.pipe_material_selector"));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("Start");
    }
}
