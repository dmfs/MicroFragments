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

package org.dmfs.android.microfragments.demo.microfragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.demo.R;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.XFaded;
import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.android.microwizard.box.Unboxed;

import java.net.URI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * @author Marten Gajda
 */
public final class IntermediateLoaderMicroFragment implements MicroFragment<IntermediateLoaderMicroFragment.Params>
{
    private final Params mArgument;


    public interface Params
    {
        Integer data();

        MicroWizard<URI> nextStep();
    }


    public IntermediateLoaderMicroFragment(final Integer argument, final MicroWizard<URI> mNext)
    {
        mArgument = new Params()
        {
            @Override
            public Integer data()
            {
                return argument;
            }


            @Override
            public MicroWizard<URI> nextStep()
            {
                return mNext;
            }
        };
    }


    @Override
    public String title(@NonNull Context context)
    {
        return "Loading …";
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host)
    {
        return new LoadFragment();
    }


    @NonNull
    @Override
    public Params parameter()
    {
        return mArgument;
    }


    @Override
    public boolean skipOnBack()
    {
        return true;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mArgument.data());
        dest.writeParcelable(mArgument.nextStep().boxed(), flags);
    }


    public final static Creator<IntermediateLoaderMicroFragment> CREATOR = new Creator<IntermediateLoaderMicroFragment>()
    {
        @Override
        public IntermediateLoaderMicroFragment createFromParcel(Parcel source)
        {
            return new IntermediateLoaderMicroFragment(source.readInt(),
                    new Unboxed<MicroWizard<URI>>(source).value());
        }


        @Override
        public IntermediateLoaderMicroFragment[] newArray(int size)
        {
            return new IntermediateLoaderMicroFragment[size];
        }
    };


    public static class LoadFragment extends Fragment
    {
        private final static int DELAY_WAIT_MESSAGE = 2500;
        private final Handler mHandler = new Handler();


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View result = inflater.inflate(R.layout.fragment_loading, container, false);
            result.findViewById(android.R.id.message).animate().alpha(1).setStartDelay(DELAY_WAIT_MESSAGE);
            return result;
        }


        @Override
        public void onResume()
        {
            super.onResume();
            mHandler.postDelayed(mFakeLoader, DELAY_WAIT_MESSAGE * 2);
        }


        @Override
        public void onPause()
        {
            mHandler.removeCallbacks(mFakeLoader);
            super.onPause();
        }


        private final Runnable mFakeLoader = new Runnable()
        {
            @Override
            public void run()
            {
                if (isResumed())
                {
                    new FragmentEnvironment<>(LoadFragment.this)
                            .host()
                            .execute(getActivity(), new XFaded(new ForwardTransition<>(
                                    new FragmentEnvironment<Params>(LoadFragment.this).microFragment()
                                            .parameter()
                                            .nextStep()
                                            .microFragment(getContext(), URI.create("https://example.com")))));
                }
            }
        };
    }
}
