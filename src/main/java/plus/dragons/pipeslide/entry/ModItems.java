package plus.dragons.pipeslide.entry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
    public static final RegistryObject<Item> PIPE_PLAYER_PLATFORM = ITEMS.register("pipe_player_platform", () -> new BlockItem(ModBlocks.PIPE_PLAYER_PLATFORM.get(), new Item.Properties()));
    public static final RegistryObject<Item> PIPE_MOB_PLATFORM = ITEMS.register("pipe_mob_platform", () -> new BlockItem(ModBlocks.PIPE_MOB_PLATFORM.get(), new Item.Properties()));
    public static final RegistryObject<Item> PIPE_MINECART_STATION = ITEMS.register("pipe_minecart_station", () -> new BlockItem(ModBlocks.PIPE_MINECART_STATION.get(), new Item.Properties()));
    public static final RegistryObject<Item> PIPE_BOAT_DOCK = ITEMS.register("pipe_boat_dock", () -> new BlockItem(ModBlocks.PIPE_BOAT_DOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> PIPE_ITEM_RECEIVER = ITEMS.register("pipe_item_receiver", () -> new BlockItem(ModBlocks.PIPE_ITEM_RECEIVER.get(), new Item.Properties()));
    public static final RegistryObject<Item> PIPE_CURVE_ANCHOR = ITEMS.register("pipe_curve_anchor", () -> new BlockItem(ModBlocks.PIPE_CURVE_ANCHOR.get(), new Item.Properties()));

}
