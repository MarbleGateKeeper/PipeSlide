package plus.dragons.pipeslide.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.pipeslide.PipeSlide;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PipeSlide.MOD_ID);
    // PIPE
    public static final RegistryObject<CreativeModeTab> PIPE_NODE = TABS.register("general", () -> CreativeModeTab.builder()
            .icon(() -> ModItems.PIPE_CONNECTOR.get().getDefaultInstance())
            .title(Component.translatable("creativeModeTab.pipeslide.general"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((parameters, output) -> {
                output.accept(ModItems.PIPE_CONNECTOR.get());
                output.accept(ModItems.PIPE_ADJUSTOR.get());
                //output.accept(ModItems.PIPE_MATERIAL_SELECTOR.get());
                output.accept(ModItems.PIPE_NODE.get());
                output.accept(ModItems.PIPE_PLAYER_PLATFORM.get());
                output.accept(ModItems.PIPE_MOB_PLATFORM.get());
                output.accept(ModItems.PIPE_MINECART_STATION.get());
                output.accept(ModItems.PIPE_BOAT_DOCK.get());
                output.accept(ModItems.PIPE_ITEM_RECEIVER.get());
                output.accept(ModItems.PIPE_CURVE_ANCHOR.get());
            }).build());
}
