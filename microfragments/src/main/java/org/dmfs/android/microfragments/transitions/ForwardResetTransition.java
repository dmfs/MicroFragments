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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.R;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.UiTimestamp;


/**
 * A {@link FragmentTransition} that clears the back stack and starts over with a new {@link MicroFragment} using a "forward" animation.
 * <p>
 * Note, the previous fragment will be swiped out to the left (to the right for RTL languages). At present this can not be configured.
 *
 * @author Marten Gajda
 */
public final class ForwardResetTransition implements FragmentTransition, Parcelable
{
    private final MicroFragment mNextStep;
    private final Timestamp mTimestamp;


    /**
     * Creates a {@link FragmentTransition} that resets the back stack and starts over with the given {@link MicroFragment}.
     *
     * @param nextStep
     *         The initial {@link MicroFragment}.
     */
    @MainThread
    public ForwardResetTransition(@NonNull MicroFragment nextStep)
    {
        this(nextStep, new UiTimestamp());
    }


    /**
     * Creates a {@link FragmentTransition} that resets the back stack and starts over with the given {@link MicroFragment}.
     *
     * @param nextStep
     *         The initial {@link MicroFragment}.
     */
    public ForwardResetTransition(@NonNull MicroFragment nextStep, Timestamp timestamp)
    {
        mNextStep = nextStep;
        mTimestamp = timestamp;
    }


    @Override
    public Timestamp timestamp()
    {
        return mTimestamp;
    }


    @Override
    public void prepare(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
    {
        // insert an empty dummy fragment to enforce the animation that we want, otherwise the pop animation of the curent fragment would be played which is usually not what we want
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.microfragments_swipe_enter, R.anim.microfragments_swipe_exit, R.anim.microfragments_swipe_enter,
                        R.anim.microfragments_swipe_exit)
                .replace(R.id.microfragments_host, new Fragment())
                .commit();
        fragmentManager.executePendingTransactions();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    @Override
    public FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
    {
        fragmentTransaction.replace(R.id.microfragments_host, mNextStep.fragment(context, host));
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
        dest.writeParcelable(mNextStep, flags);
    }


    public final static Creator<ForwardResetTransition> CREATOR = new Creator<ForwardResetTransition>()
    {
        @Override
        public ForwardResetTransition createFromParcel(Parcel source)
        {
            ClassLoader loader = getClass().getClassLoader();
            MicroFragment<?> microFragment = source.readParcelable(loader);
            Timestamp timestamp = source.readParcelable(loader);
            return new ForwardResetTransition(microFragment, timestamp);
        }


        @Override
        public ForwardResetTransition[] newArray(int size)
        {
            return new ForwardResetTransition[size];
        }
    };
}
