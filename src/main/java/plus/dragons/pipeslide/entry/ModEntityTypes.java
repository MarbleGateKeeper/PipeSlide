package plus.dragons.pipeslide.entry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.pipeslide.PipeSlide;
import plus.dragons.pipeslide.content.pipes.PlayerCarrierEntity;
import plus.dragons.pipeslide.foundation.client.renderer.BlandEntityRenderer;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PipeSlide.MOD_ID);

    public static final RegistryObject<EntityType<PlayerCarrierEntity>> CARRIER = register("carrier", PlayerCarrierEntity::new);

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, EntityType.EntityFactory<T> factory){
        return ENTITY_TYPES.register(id, () -> EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(0.1F, 0.1F).clientTrackingRange(8).build(id));
    }

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.CARRIER.get(), BlandEntityRenderer::new);
    }
}
