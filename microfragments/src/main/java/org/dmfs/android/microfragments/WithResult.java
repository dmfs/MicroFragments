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

package org.dmfs.android.microfragments;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @author Marten Gajda
 */
public final class WithResult<T> implements MicroFragmentEnvironment<T>, Parcelable
{
    private final MicroFragmentEnvironment<T> mDelegate;
    private final Parcelable mResult;


    public WithResult(@NonNull MicroFragmentEnvironment<T> delegate, @Nullable Parcelable result)
    {
        mDelegate = delegate;
        mResult = result;
    }


    @NonNull
    @Override
    public MicroFragment<T> microFragment()
    {
        return mDelegate.microFragment();
    }


    @NonNull
    @Override
    public MicroFragmentHost host()
    {
        return mDelegate.host();
    }


    @Nullable
    @Override
    public Parcelable result()
    {
        return mResult;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mDelegate, flags);
        dest.writeParcelable(mResult, flags);
    }


    public final static Creator<WithResult<?>> CREATOR = new Creator<WithResult<?>>()
    {
        @Override
        public WithResult<?> createFromParcel(Parcel source)
        {
            ClassLoader classLoader = getClass().getClassLoader();
            return new WithResult<>((MicroFragmentEnvironment<?>) source.readParcelable(classLoader), source.readParcelable(classLoader));
        }


        @Override
        public WithResult<?>[] newArray(int size)
        {
            return new WithResult<?>[size];
        }
    };
}
