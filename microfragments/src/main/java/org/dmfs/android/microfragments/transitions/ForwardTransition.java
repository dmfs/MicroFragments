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
import org.dmfs.android.microfragments.R;


/**
 * A {@link FragmentTransition} that moves on to the next {@link MicroFragment}.
 *
 * @author Marten Gajda
 */
public final class ForwardTransition implements FragmentTransition, Parcelable
{
    private final MicroFragment mNextStep;


    /**
     * Creates a {@link FragmentTransition} that swipes to the given {@link MicroFragment}.
     *
     * @param nextStep
     *         The next {@link MicroFragment}.
     */
    public ForwardTransition(@NonNull MicroFragment nextStep)
    {
        mNextStep = nextStep;
    }


    @Override
    public void prepare(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
    {

    }


    @Override
    public FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
    {
        fragmentTransaction.replace(R.id.microfragments_host, mNextStep.fragment(context, host));
        if (fragmentManager.getBackStackEntryCount() > 0 || !previousStep.skipOnBack())
        {
            fragmentTransaction.addToBackStack(mNextStep.skipOnBack() ? "skip" : null);
        }
        return fragmentTransaction;
    }


    @Override
    public void cleanup(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment previousStep)
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
            return new ForwardTransition(microFragment);
        }


        @Override
        public ForwardTransition[] newArray(int size)
        {
            return new ForwardTransition[size];
        }
    };
}
