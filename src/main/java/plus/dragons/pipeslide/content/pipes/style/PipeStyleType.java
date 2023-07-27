package plus.dragons.pipeslide.content.pipes.style;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import plus.dragons.pipeslide.PipeSlide;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PipeStyleType<T extends IPipeStyle> {
    public static final Map<ResourceLocation, PipeStyleType<?>> TYPES = new HashMap<>();
    private final ResourceLocation id;
    private final Function<CompoundTag, T> factory;

    public static final PipeStyleType<IPipeStyle> DEFAULT =
            register(PipeSlide.genRL("default"), (compoundTag) -> MarbleWhiteStyle.INSTANCE);

    public static <T extends IPipeStyle> PipeStyleType<T> register(ResourceLocation id, Function<CompoundTag, T> factory) {
        PipeStyleType<T> type = new PipeStyleType<>(id, factory);
        TYPES.put(id, type);
        return type;
    }

    public PipeStyleType(ResourceLocation id, Function<CompoundTag, T> factory) {
        this.id = id;
        this.factory = factory;
    }

    public ResourceLocation getId() {
        return id;
    }

    public T fromTag(CompoundTag tag) {
        return factory.apply(tag);
    }
}
