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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.utility.Iterate
 */
public class Iterate {

    public static final boolean[] trueAndFalse = { true, false };
    public static final boolean[] falseAndTrue = { false, true };
    public static final int[] zeroAndOne = { 0, 1 };
    public static final int[] positiveAndNegative = { 1, -1 };
    public static final Direction[] directions = Direction.values();
    public static final Direction[] horizontalDirections = getHorizontals();
    public static final Direction.Axis[] axes = Direction.Axis.values();
    public static final EnumSet<Direction.Axis> axisSet = EnumSet.allOf(Direction.Axis.class);

    private static Direction[] getHorizontals() {
        Direction[] directions = new Direction[4];
        for (int i = 0; i < 4; i++)
            directions[i] = Direction.from2DDataValue(i);
        return directions;
    }

    public static Direction[] directionsInAxis(Direction.Axis axis) {
        switch (axis) {
            case X:
                return new Direction[] { Direction.EAST, Direction.WEST };
            case Y:
                return new Direction[] { Direction.UP, Direction.DOWN };
            default:
            case Z:
                return new Direction[] { Direction.SOUTH, Direction.NORTH };
        }
    }

    public static List<BlockPos> hereAndBelow(BlockPos pos) {
        return Arrays.asList(pos, pos.below());
    }

    public static List<BlockPos> hereBelowAndAbove(BlockPos pos) {
        return Arrays.asList(pos, pos.below(), pos.above());
    }
}
