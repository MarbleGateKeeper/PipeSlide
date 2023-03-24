/*
MIT License

Copyright (c) 2019 simibubi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package plus.dragons.pipeslide.foundation.utility;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.utility.NBTHelper
 */
public class NBTHelper {

    public static <T extends Enum<?>> T readEnum(CompoundTag nbt, String key, Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null)
            throw new IllegalArgumentException("Non-Enum class passed to readEnum: " + enumClass.getName());
        if (nbt.contains(key, Tag.TAG_STRING)) {
            String name = nbt.getString(key);
            for (T t : enumConstants) {
                if (t.name()
                        .equals(name))
                    return t;
            }
        }
        return enumConstants[0];
    }

    public static <T extends Enum<?>> void writeEnum(CompoundTag nbt, String key, T enumConstant) {
        nbt.putString(key, enumConstant.name());
    }

    public static <T> ListTag writeCompoundList(Iterable<T> list, Function<T, CompoundTag> serializer) {
        ListTag listNBT = new ListTag();
        list.forEach(t -> {
            CompoundTag apply = serializer.apply(t);
            if (apply == null)
                return;
            listNBT.add(apply);
        });
        return listNBT;
    }

    public static <T> List<T> readCompoundList(ListTag listNBT, Function<CompoundTag, T> deserializer) {
        List<T> list = new ArrayList<>(listNBT.size());
        listNBT.forEach(inbt -> list.add(deserializer.apply((CompoundTag) inbt)));
        return list;
    }

    public static <T> void iterateCompoundList(ListTag listNBT, Consumer<CompoundTag> consumer) {
        listNBT.forEach(inbt -> consumer.accept((CompoundTag) inbt));
    }

    public static ListTag writeItemList(Iterable<ItemStack> stacks) {
        return writeCompoundList(stacks, ItemStack::serializeNBT);
    }

    public static List<ItemStack> readItemList(ListTag stacks) {
        return readCompoundList(stacks, ItemStack::of);
    }

    public static ListTag writeAABB(AABB bb) {
        ListTag bbtag = new ListTag();
        bbtag.add(FloatTag.valueOf((float) bb.minX));
        bbtag.add(FloatTag.valueOf((float) bb.minY));
        bbtag.add(FloatTag.valueOf((float) bb.minZ));
        bbtag.add(FloatTag.valueOf((float) bb.maxX));
        bbtag.add(FloatTag.valueOf((float) bb.maxY));
        bbtag.add(FloatTag.valueOf((float) bb.maxZ));
        return bbtag;
    }

    public static AABB readAABB(ListTag bbtag) {
        if (bbtag == null || bbtag.isEmpty())
            return null;
        return new AABB(bbtag.getFloat(0), bbtag.getFloat(1), bbtag.getFloat(2), bbtag.getFloat(3),
                bbtag.getFloat(4), bbtag.getFloat(5));
    }

    public static ListTag writeVec3i(Vec3i vec) {
        ListTag tag = new ListTag();
        tag.add(IntTag.valueOf(vec.getX()));
        tag.add(IntTag.valueOf(vec.getY()));
        tag.add(IntTag.valueOf(vec.getZ()));
        return tag;
    }

    public static Vec3i readVec3i(ListTag tag) {
        return new Vec3i(tag.getInt(0), tag.getInt(1), tag.getInt(2));
    }

    @Nonnull
    public static Tag getINBT(CompoundTag nbt, String id) {
        Tag inbt = nbt.get(id);
        if (inbt != null)
            return inbt;
        return new CompoundTag();
    }

}