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

import java.util.Objects;

/**
 * Reference:
 * Version: com.simibubi.create:create-1.19.2:0.5.0.i-23
 * Class: com.simibubi.create.foundation.utility.Pair
 */
public class Pair<F, S> {

    F first;
    S second;

    protected Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public Pair<F, S> copy() {
        return Pair.of(first, second);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Pair) {
            final Pair<?, ?> other = (Pair<?, ?>) obj;
            return Objects.equals(first, other.first) && Objects.equals(second, other.second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (nullHash(first) * 31) ^ nullHash(second);
    }

    int nullHash(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    public Pair<S, F> swap() {
        return Pair.of(second, first);
    }

}
