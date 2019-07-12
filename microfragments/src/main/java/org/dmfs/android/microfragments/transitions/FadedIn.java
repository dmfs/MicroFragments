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

package org.dmfs.android.microfragments.transitions;

import android.content.Context;
import android.os.Parcel;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.R;
import org.dmfs.android.microfragments.Timestamp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * A decorator for {@link ForwardTransition}s and {@link OverlayTransition}s. It adds an animation to the transition that fades the old fragment out to reveal
 * the new one.
 *
 * @author Marten Gajda
 */
public final class FadedIn implements FragmentTransition
{
    private final FragmentTransition mDelegate;


    public FadedIn(@NonNull FragmentTransition delegate)
    {
        mDelegate = delegate;
    }


    @NonNull
    @Override
    public Timestamp timestamp()
    {
        return mDelegate.timestamp();
    }


    @Override
    public void prepare(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        mDelegate.prepare(context, fragmentManager, host, previousStep);
    }


    @NonNull
    @Override
    public FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        fragmentTransaction.setCustomAnimations(R.anim.microfragments_fade_enter, R.anim.microfragments_none, R.anim.microfragments_none,
                R.anim.microfragments_fade_exit);
        return mDelegate.updateTransaction(context, fragmentTransaction, fragmentManager, host, previousStep);
    }


    @Override
    public void cleanup(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        mDelegate.cleanup(context, fragmentManager, host, previousStep);
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
    }


    public final static Creator<FadedIn> CREATOR = new Creator<FadedIn>()
    {
        @Override
        public FadedIn createFromParcel(Parcel source)
        {
            return new FadedIn((FragmentTransition) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public FadedIn[] newArray(int size)
        {
            return new FadedIn[size];
        }
    };
}
