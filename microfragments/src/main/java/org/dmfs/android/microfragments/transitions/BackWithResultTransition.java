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
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.pigeonpost.Cage;


/**
 * A {@link FragmentTransition} that returns to the previous {@link MicroFragment} but also provides a result to it.
 *
 * @author Marten Gajda
 */
public final class BackWithResultTransition<T extends Parcelable> implements FragmentTransition
{
    @NonNull
    private final Cage<T> mCage;

    @NonNull
    private final Timestamp mTimestamp;

    @Nullable
    private final T mResult;


    /**
     * Creates a {@link FragmentTransition} that goes back to the previous {@link MicroFragment}.
     */
    @MainThread
    public BackWithResultTransition(@NonNull Cage<T> cage, @Nullable T result)
    {
        this(cage, result, new UiTimestamp());
    }


    /**
     * Creates a {@link FragmentTransition} that goes back to the previous {@link MicroFragment}.
     */
    public BackWithResultTransition(@NonNull Cage<T> cage, @Nullable T result, @NonNull Timestamp timestamp)
    {
        mCage = cage;
        mResult = result;
        mTimestamp = timestamp;
    }


    @NonNull
    @Override
    public Timestamp timestamp()
    {
        return mTimestamp;
    }


    @Override
    public void prepare(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        fragmentManager.popBackStackImmediate();
        // make sure we also skip all skipable steps.
        if (fragmentManager.getBackStackEntryCount() > 0
                && "skip".equals(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName()))
        {
            fragmentManager.popBackStackImmediate("skip", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    @NonNull
    @Override
    public FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        // nothing to add
        return fragmentTransaction;
    }


    @Override
    public void cleanup(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        mCage.pigeon(mResult).send(context);
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mCage, flags);
        dest.writeParcelable(mResult, flags);
        dest.writeParcelable(mTimestamp, flags);
    }


    public final static Creator<BackWithResultTransition> CREATOR = new Creator<BackWithResultTransition>()
    {
        @Override
        public BackWithResultTransition createFromParcel(Parcel source)
        {
            ClassLoader classLoader = getClass().getClassLoader();
            return new BackWithResultTransition<>((Cage) source.readParcelable(classLoader), source.readParcelable(classLoader),
                    (Timestamp) source.readParcelable(classLoader));
        }


        @Override
        public BackWithResultTransition[] newArray(int size)
        {
            return new BackWithResultTransition[size];
        }
    };
}
