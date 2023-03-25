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
import plus.dragons.pipeslide.content.pipes.PipeNodeConnectorItem;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PipeSlide.MOD_ID);

    public static final RegistryObject<Item> PIPE_NODE_CONNECTOR = ITEMS.register("pipe_node_connector", PipeNodeConnectorItem::new);

    // Pipe
    public static final RegistryObject<Item> PIPE_NODE = ITEMS.register("pipe_node", () -> new BlockItem(ModBlocks.PIPE_NODE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PIPE_CURVE_ANCHOR = ITEMS.register("pipe_curve_anchor", () -> new BlockItem(ModBlocks.PIPE_CURVE_ANCHOR.get(), new Item.Properties()));

    public static void registerCreativeTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(PipeSlide.MOD_ID, "general"), builder ->
                builder.icon(() -> new ItemStack(PIPE_NODE.get()))
                        .displayItems((parameters, output) -> {
                            output.accept(PIPE_NODE_CONNECTOR.get());
                            output.accept(PIPE_NODE.get());
                            output.accept(PIPE_CURVE_ANCHOR.get());
                        })
        );
    }
}
