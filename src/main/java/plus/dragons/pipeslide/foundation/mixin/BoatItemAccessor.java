package plus.dragons.pipeslide.foundation.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BoatItem.class)
public interface BoatItemAccessor {
    @Accessor
    Boat.Type getType();

    @Invoker
    Boat invokeGetBoat(Level pLevel, HitResult pHitResult);
}
