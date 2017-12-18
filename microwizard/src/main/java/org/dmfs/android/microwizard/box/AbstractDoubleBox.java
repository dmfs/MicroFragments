/*
 * Copyright (c) 2017 dmfs GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.android.microwizard.box;

import android.os.Parcel;

import org.dmfs.jems.function.BiFunction;
import org.dmfs.jems.function.Function;


/**
 * An abstract {@link Box} of a {@link Boxable} class containing two other {@link Boxable}s.
 * TODO: move to boxed-bolts
 *
 * @param <U>
 * @param <V>
 * @param <T>
 *
 * @author Marten Gajda
 */
public abstract class AbstractDoubleBox<U extends Boxable<U>, V extends Boxable<V>, T extends Boxable<T>> implements Box<T>
{
    private final U mBoxableA;
    private final V mBoxableB;
    private final BiFunction<U, V, T> mFunction;


    protected AbstractDoubleBox(U boxableA, V boxableB, BiFunction<U, V, T> function)
    {
        mBoxableA = boxableA;
        mBoxableB = boxableB;
        mFunction = function;
    }


    public final int describeContents()
    {
        return 0;
    }


    public final void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mBoxableA.boxed(), flags);
        dest.writeParcelable(mBoxableB.boxed(), flags);
    }


    public final T value()
    {
        return mFunction.value(mBoxableA, mBoxableB);
    }


    public static final class DoubleBoxableBoxCreator<U extends Boxable<U>, V extends Boxable<V>, T extends Boxable<T>> implements Creator<Box<T>>
    {
        private final BiFunction<U, V, Box<T>> mFunction;
        private final Function<Integer, Box<T>[]> mArrayFunction;


        public DoubleBoxableBoxCreator(BiFunction<U, V, Box<T>> function, Function<Integer, Box<T>[]> arrayFunction)
        {
            mFunction = function;
            mArrayFunction = arrayFunction;
        }


        public final Box<T> createFromParcel(Parcel source)
        {
            return mFunction.value((new Unboxed<U>(source)).value(), (new Unboxed<V>(source)).value());
        }


        public final Box<T>[] newArray(int size)
        {
            return mArrayFunction.value(size);
        }
    }
}
