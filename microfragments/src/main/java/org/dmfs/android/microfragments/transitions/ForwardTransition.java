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
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.dmfs.android.microfragments.BasicMicroFragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.R;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static org.dmfs.android.microfragments.MicroFragment.ARG_ENVIRONMENT;


/**
 * A {@link FragmentTransition} that moves on to the next {@link MicroFragment} in a forward transition.
 *
 * @author Marten Gajda
 */
public final class ForwardTransition<T> implements FragmentTransition, Parcelable
{
    private final MicroFragment<T> mNextStep;
    private final Timestamp mTimestamp;


    /**
     * Creates a {@link FragmentTransition} that swipes to the given {@link MicroFragment}.
     *
     * @param nextStep
     *         The next {@link MicroFragment}.
     */
    @MainThread
    public ForwardTransition(@NonNull MicroFragment<T> nextStep)
    {
        this(nextStep, new UiTimestamp());
    }


    /**
     * Creates a {@link FragmentTransition} that swipes to the given {@link MicroFragment}.
     *
     * @param nextStep
     *         The next {@link MicroFragment}.
     */
    public ForwardTransition(@NonNull MicroFragment<T> nextStep, Timestamp timestamp)
    {
        mNextStep = nextStep;
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

    }


    @NonNull
    @Override
    public FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        Fragment fragment = mNextStep.fragment(context, host);

        Bundle args = new Bundle(1);
        args.putParcelable(ARG_ENVIRONMENT, new BasicMicroFragmentEnvironment<>(mNextStep, host));
        fragment.setArguments(args);

        fragmentTransaction.replace(R.id.microfragments_host, fragment);
        if (fragmentManager.getBackStackEntryCount() > 0 || !previousStep.skipOnBack())
        {
            fragmentTransaction.addToBackStack(mNextStep.skipOnBack() ? "skip" : "noskip");
        }
        return fragmentTransaction;
    }


    @Override
    public void cleanup(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {

    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mNextStep, flags);
    }


    public final static Parcelable.Creator<ForwardTransition> CREATOR = new Parcelable.Creator<ForwardTransition>()
    {
        @Override
        public ForwardTransition createFromParcel(Parcel source)
        {
            ClassLoader loader = getClass().getClassLoader();
            MicroFragment<?> microFragment = source.readParcelable(loader);
            Timestamp timestamp = source.readParcelable(loader);
            return new ForwardTransition<>(microFragment, timestamp);
        }


        @Override
        public ForwardTransition[] newArray(int size)
        {
            return new ForwardTransition[size];
        }
    };
}
