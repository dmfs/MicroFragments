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

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;

import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.pigeonpost.Cage;


/**
 * @author Marten Gajda
 */
public final class BasicMicroFragmentHost implements MicroFragmentHost
{
    private final Cage<FragmentTransition> mFragmentTransitionCage;


    public BasicMicroFragmentHost(@NonNull Cage<FragmentTransition> fragmentTransitionCage)
    {
        mFragmentTransitionCage = fragmentTransitionCage;
    }


    @Override
    public void execute(@NonNull Context context, @NonNull FragmentTransition fragmentTransition)
    {
        mFragmentTransitionCage.pigeon(fragmentTransition).send(context);
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mFragmentTransitionCage, flags);
    }


    public final static Creator<BasicMicroFragmentHost> CREATOR = new Creator<BasicMicroFragmentHost>()
    {
        @Override
        public BasicMicroFragmentHost createFromParcel(Parcel source)
        {
            return new BasicMicroFragmentHost((Cage<FragmentTransition>) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public BasicMicroFragmentHost[] newArray(int size)
        {
            return new BasicMicroFragmentHost[size];
        }
    };
}
