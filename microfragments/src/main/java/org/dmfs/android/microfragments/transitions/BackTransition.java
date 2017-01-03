/*
 * Copyright 2016 dmfs GmbH
 *
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

package org.dmfs.android.microfragments.transitions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;


/**
 * A {@link FragmentTransition} that returns to the previous {@link MicroFragment}.
 *
 * @author Marten Gajda
 */
public final class BackTransition implements FragmentTransition
{

    /**
     * Creates a {@link FragmentTransition} that goes back to the previous {@link MicroFragment}.
     */
    public BackTransition()
    {
    }


    @Override
    public void prepare(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
    {
        fragmentManager.popBackStackImmediate();
    }


    @Override
    public FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
    {
        // nothing to add here
        return fragmentTransaction;
    }


    @Override
    public void cleanup(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
    {
        // nothing to be done
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // nothing to write here
    }


    public final static Parcelable.Creator<BackTransition> CREATOR = new Parcelable.Creator<BackTransition>()
    {
        @Override
        public BackTransition createFromParcel(Parcel source)
        {
            return new BackTransition();
        }


        @Override
        public BackTransition[] newArray(int size)
        {
            return new BackTransition[size];
        }
    };
}