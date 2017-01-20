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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.demo.R;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.Swiped;


/**
 * Created by marten on 09.12.16.
 */
public final class MicroFragment1 implements MicroFragment<String>
{

    private final String mName;


    public MicroFragment1(String name)
    {
        mName = name;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        return mName;
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host)
    {
        return new Step1Fragment();
    }


    @NonNull
    @Override
    public String parameter()
    {
        return mName;
    }


    @Override
    public boolean skipOnBack()
    {
        return false;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mName);
    }


    public final static Creator<MicroFragment1> CREATOR = new Creator<MicroFragment1>()
    {
        @Override
        public MicroFragment1 createFromParcel(Parcel source)
        {
            return new MicroFragment1(source.readString());
        }


        @Override
        public MicroFragment1[] newArray(int size)
        {
            return new MicroFragment1[size];
        }
    };


    /**
     * A simple {@link Fragment} subclass.
     */
    public static class Step1Fragment extends Fragment implements View.OnClickListener
    {

        private MicroFragmentEnvironment<String> mEnvironment;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mEnvironment = new FragmentEnvironment<>(this);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_step1, container, false);

            ((TextView) view.findViewById(R.id.text)).setText(getActivity().getString(R.string.hello_fragment, mEnvironment.microFragment().parameter()));
            view.findViewById(android.R.id.button1).setOnClickListener(this);
            return view;
        }


        @Override
        public void onClick(View v)
        {
            MicroFragmentEnvironment<?> environment = getArguments().getParcelable(MicroFragment.ARG_ENVIRONMENT);
            environment.host().execute(getActivity(), new Swiped(new ForwardTransition<>(new IntermediateLoaderMicroFragment())));
        }
    }

}
