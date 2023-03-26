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

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.utility.AngleHelper
 */
public class AngleHelper {

    public static float horizontalAngle(Direction facing) {
        if (facing.getAxis().isVertical())
            return 0;
        float angle = facing.toYRot();
        if (facing.getAxis() == Direction.Axis.X)
            angle = -angle;
        return angle;
    }

    public static float verticalAngle(Direction facing) {
        return facing == Direction.UP ? -90 : facing == Direction.DOWN ? 90 : 0;
    }

    public static float rad(double angle) {
        if (angle == 0)
            return 0;
        return (float) (angle / 180 * Math.PI);
    }

    public static float deg(double angle) {
        if (angle == 0)
            return 0;
        return (float) (angle * 180 / Math.PI);
    }

    public static float angleLerp(double pct, double current, double target) {
        return (float) (current + getShortestAngleDiff(current, target) * pct);
    }

    public static float getShortestAngleDiff(double current, double target) {
        current = current % 360;
        target = target % 360;
        return (float) (((((target - current) % 360) + 540) % 360) - 180);
    }

    public static float getShortestAngleDiff(double current, double target, float hint) {
        float diff = getShortestAngleDiff(current, target);
        if (Mth.equal(Math.abs(diff), 180) && Math.signum(diff) != Math.signum(hint)) {
            return diff + 360*Math.signum(hint);
        }
        return diff;
    }

}