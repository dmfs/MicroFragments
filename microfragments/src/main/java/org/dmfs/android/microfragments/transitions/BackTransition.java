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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.utils.DeadPigeonCage;
import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Pigeon;


/**
 * A {@link FragmentTransition} that returns to the previous non-skipable {@link MicroFragment} on the backstack, if there is any.
 * <p>
 * This transition takes an optional {@link Cage} argument to send the result of the back operation (i.e. whether there was a previous {@link MicroFragment} to
 * go back to) to the caller.
 *
 * @author Marten Gajda
 */
public final class BackTransition implements FragmentTransition
{

    private final Timestamp mTimestamp;
    private final Cage<Boolean> mResponseCage;


    /**
     * Creates a {@link FragmentTransition} that goes back to the previous {@link MicroFragment}.
     */
    @MainThread
    public BackTransition()
    {
        this(new UiTimestamp(), DeadPigeonCage.<Boolean>instance());
    }


    /**
     * Creates a {@link FragmentTransition} that goes back to the previous {@link MicroFragment} using the given {@link Timestamp}.
     *
     * @param timestamp
     *         The {@link Timestamp} of the origin of this transition.
     */
    public BackTransition(@NonNull Timestamp timestamp)
    {
        this(timestamp, DeadPigeonCage.<Boolean>instance());
    }


    /**
     * Creates a {@link FragmentTransition} that goes back to the previous {@link MicroFragment}.
     *
     * @param responseCage
     *         A {@link Cage} for {@link Boolean} {@link Pigeon}s to deliver the result of the operation.
     */
    @MainThread
    public BackTransition(@NonNull Cage<Boolean> responseCage)
    {
        this(new UiTimestamp(), responseCage);
    }


    /**
     * Creates a {@link FragmentTransition} that goes back to the previous {@link MicroFragment} using the given {@link Timestamp}.
     *
     * @param timestamp
     *         The {@link Timestamp} of the origin of this transition.
     * @param responseCage
     *         A {@link Cage} for {@link Boolean} {@link Pigeon}s to deliver the result of the operation.
     */
    public BackTransition(@NonNull Timestamp timestamp, @NonNull Cage<Boolean> responseCage)
    {
        mTimestamp = timestamp;
        mResponseCage = responseCage;
    }


    @NonNull
    @Override
    public Timestamp timestamp()
    {
        return mTimestamp;
    }


    @Override
    public void prepare(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
    {
        mResponseCage.pigeon(fragmentManager.popBackStackImmediate()).send(context);
    }


    @NonNull
    @Override
    public FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
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
        dest.writeParcelable(mTimestamp, flags);
    }


    public final static Parcelable.Creator<BackTransition> CREATOR = new Parcelable.Creator<BackTransition>()
    {
        @Override
        public BackTransition createFromParcel(Parcel source)
        {
            return new BackTransition((Timestamp) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public BackTransition[] newArray(int size)
        {
            return new BackTransition[size];
        }
    };
}
