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
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.demo.R;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.transitions.ForwardResetTransition;
import org.dmfs.android.microfragments.transitions.Swiped;
import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.android.microwizard.box.Unboxed;

import java.net.URI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * @author Marten Gajda
 */
public final class FinalLoaderMicroFragment implements MicroFragment<FinalLoaderMicroFragment.Params>
{
    private final Params mArguments;


    public interface Params
    {
        URI uri();

        MicroWizard<String> next();
    }


    public FinalLoaderMicroFragment(URI uri, MicroWizard<String> next)
    {
        mArguments = new Params()
        {
            @Override
            public URI uri()
            {
                return uri;
            }


            @Override
            public MicroWizard<String> next()
            {
                return next;
            }
        };
    }


    @Override
    public String title(@NonNull Context context)
    {
        return "Loading again …";
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
        return mArguments;
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
        dest.writeSerializable(mArguments.uri());
        dest.writeParcelable(mArguments.next().boxed(), flags);
    }


    public final static Creator<FinalLoaderMicroFragment> CREATOR = new Creator<FinalLoaderMicroFragment>()
    {
        @Override
        public FinalLoaderMicroFragment createFromParcel(Parcel source)
        {
            return new FinalLoaderMicroFragment((URI) source.readSerializable(), new Unboxed<MicroWizard<String>>(source).value());
        }


        @Override
        public FinalLoaderMicroFragment[] newArray(int size)
        {
            return new FinalLoaderMicroFragment[size];
        }
    };


    public static class LoadFragment extends Fragment
    {
        private final static int DELAY_WAIT_MESSAGE = 2500;
        private final Timestamp mTimestamp = new UiTimestamp();


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
        }


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
            new Loader(mTimestamp).start();
        }


        private final class Loader extends Thread
        {
            private final Timestamp mTimestamp;


            private Loader(Timestamp timestamp)
            {
                mTimestamp = timestamp;
            }


            @Override
            public void run()
            {
                try
                {
                    sleep(DELAY_WAIT_MESSAGE * 3 / 2);
                }
                catch (InterruptedException e)
                {
                }

                if (isResumed())
                {
                    MicroFragmentEnvironment<Params> environment = new FragmentEnvironment<>(LoadFragment.this);
                    environment.host()
                            .execute(getActivity(), new Swiped(
                                    new ForwardResetTransition<>(environment.microFragment().parameter().next().microFragment(getActivity(), "Result"),
                                            mTimestamp)));
                }
            }
        }
    }
}
