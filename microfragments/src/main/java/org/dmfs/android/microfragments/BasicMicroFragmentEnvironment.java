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


public final class BasicMicroFragmentEnvironment<T> implements MicroFragmentEnvironment<T>
{
    private final MicroFragment<T> microFragment;
    private final MicroFragmentHost host;


    public BasicMicroFragmentEnvironment(MicroFragment<T> microFragment, MicroFragmentHost host)
    {
        this.host = host;
        this.microFragment = microFragment;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(microFragment, flags);
        dest.writeParcelable(host, flags);
    }


    @Override
    public MicroFragment<T> microFragment()
    {
        return microFragment;
    }


    @Override
    public MicroFragmentHost host()
    {
        return host;
    }


    @Override
    public Parcelable result()
    {
        // no result yet
        return null;
    }


    public final static Creator<BasicMicroFragmentEnvironment<?>> CREATOR = new Creator<BasicMicroFragmentEnvironment<?>>()
    {
        @Override
        public BasicMicroFragmentEnvironment<?> createFromParcel(Parcel source)
        {
            ClassLoader classLoader = getClass().getClassLoader();
            return new BasicMicroFragmentEnvironment<>((MicroFragment<?>) source.readParcelable(classLoader),
                    (MicroFragmentHost) source.readParcelable(classLoader));
        }


        @Override
        public BasicMicroFragmentEnvironment<?>[] newArray(int size)
        {
            return new BasicMicroFragmentEnvironment<?>[0];
        }
    };
}