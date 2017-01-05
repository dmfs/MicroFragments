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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;


/**
 * The {@link FragmentEnvironment} of a specific {@link Fragment}.
 *
 * @author Marten Gajda
 */
public class FragmentEnvironment<T> implements MicroFragmentEnvironment<T>
{
    private final Bundle mArguments;


    public FragmentEnvironment(Fragment fragment)
    {
        this(fragment.getArguments());
    }


    public FragmentEnvironment(Bundle arguments)
    {
        mArguments = arguments;
    }


    @Override
    public MicroFragment<T> microFragment()
    {
        return delegate().microFragment();
    }


    @Override
    public MicroFragmentHost host()
    {
        return delegate().host();
    }


    private MicroFragmentEnvironment<T> delegate()
    {
        return mArguments.getParcelable(MicroFragment.ARG_ENVIRONMENT);
    }


    @Override
    public Parcelable result()
    {
        return delegate().result();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeBundle(mArguments);
    }


    public final static Creator<FragmentEnvironment<?>> CREATOR = new Creator<FragmentEnvironment<?>>()
    {
        @Override
        public FragmentEnvironment<?> createFromParcel(Parcel source)
        {
            return new FragmentEnvironment<>(source.readBundle());
        }


        @Override
        public FragmentEnvironment<?>[] newArray(int size)
        {
            return new FragmentEnvironment<?>[size];
        }
    };
}
