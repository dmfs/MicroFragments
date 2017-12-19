/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.android.microwizard.tools;

import android.os.Parcel;

import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.jems.function.Function;


/**
 * A {@link Box} for simple {@link MicroWizard}s with exactly one follow up {@link MicroWizard} (i.a. a "linear wizard").
 * <p>
 * NOTE: this class is abstract because you need to be override it in order to add the static CREATOR field (using {@link FollowUpMicroWizardCreator}.
 *
 * @author Marten Gajda
 */
public abstract class AbstractFollowUpMicroWizardBox<T, V> implements Box<MicroWizard<T>>
{
    private final MicroWizard<V> mNext;
    private final Function<MicroWizard<V>, MicroWizard<T>> mFunction;


    protected AbstractFollowUpMicroWizardBox(MicroWizard<V> next, Function<MicroWizard<V>, MicroWizard<T>> function)
    {
        mNext = next;
        mFunction = function;
    }


    @Override
    public final int describeContents()
    {
        return 0;
    }


    @Override
    public final void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mNext.boxed(), flags);
    }


    @Override
    public final MicroWizard<T> value()
    {
        return mFunction.value(mNext);
    }


    public final static class FollowUpMicroWizardCreator<V, T> implements Creator<MicroWizard<T>>
    {
        private final Function<MicroWizard<V>, MicroWizard<T>> mFunction;
        private final Function<Integer, MicroWizard<T>[]> mArrayFunction;


        public FollowUpMicroWizardCreator(Function<MicroWizard<V>, MicroWizard<T>> function, Function<Integer, MicroWizard<T>[]> arrayFunction)
        {
            mFunction = function;
            mArrayFunction = arrayFunction;
        }


        @Override
        public final MicroWizard<T> createFromParcel(Parcel source)
        {
            return mFunction.value(new Unboxed<MicroWizard<V>>(source).value());
        }


        @Override
        public final MicroWizard<T>[] newArray(int size)
        {
            return mArrayFunction.value(size);
        }

    }
}
