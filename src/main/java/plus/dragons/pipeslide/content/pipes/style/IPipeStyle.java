package plus.dragons.pipeslide.content.pipes.style;

import net.minecraft.nbt.CompoundTag;

public interface IPipeStyle {
    CompoundTag write();

    PipeStyleType<?> getType();

    IPipeStyle EMPTY = new IPipeStyle() {
        @Override
        public CompoundTag write() {
            return new CompoundTag();
        }

        @Override
        public PipeStyleType<?> getType() {
            return PipeStyleType.DEFAULT;
        }
    };
}
