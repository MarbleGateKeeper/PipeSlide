package plus.dragons.pipeslide.content.pipes;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import plus.dragons.pipeslide.content.carrier.CarrierEntity;

public interface INavigationPipeBE {

    /**
     * Return next T
     */
    Result navigate(CarrierEntity carrier, BlockPos nextNode, float speed, float currentT);

    @Nullable
    BlockPos getNextNode(BlockPos from);

    /**
     * @param navigatorNext null when not available
     * @param nextNode      null when not available
     */
    record Result(@Nullable INavigationPipeBE navigatorNext, @Nullable BlockPos nextNode, float speed, float t) {
    }
}
