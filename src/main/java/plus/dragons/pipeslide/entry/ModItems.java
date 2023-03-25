package plus.dragons.pipeslide.entry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.pipeslide.PipeSlide;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PipeSlide.MOD_ID);

    // Pipe
    public static final RegistryObject<Item> PIPE_NODE = ITEMS.register("pipe_node", () -> new BlockItem(ModBlocks.PIPE_NODE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PIPE_CURVE_ARCHER = ITEMS.register("pipe_curve_archer", () -> new BlockItem(ModBlocks.PIPE_CURVE_ARCHER.get(), new Item.Properties()));

    public static void registerCreativeTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(PipeSlide.MOD_ID, "general"), builder ->
                builder.icon(() -> new ItemStack(PIPE_NODE.get()))
                        .displayItems((parameters, output) -> {
                            output.accept(PIPE_NODE.get());
                            output.accept(PIPE_CURVE_ARCHER.get());
                        })
        );
    }
}
