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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.demo.R;


/**
 * Created by marten on 09.12.16.
 */
public final class MicroFragment implements org.dmfs.android.microfragments.MicroFragment<Void>
{
    @Override
    public String title(Context context)
    {
        return "Last Step";
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host)
    {
        Fragment fragment = new Step1Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MICRO_FRAGMENT, this);
        args.putParcelable("host", host);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Void parameters()
    {
        return null;
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

    }


    public Creator<org.dmfs.android.microfragments.demo.microfragments.MicroFragment> CREATOR = new Creator<org.dmfs.android.microfragments.demo.microfragments.MicroFragment>()
    {
        @Override
        public org.dmfs.android.microfragments.demo.microfragments.MicroFragment createFromParcel(Parcel source)
        {
            return new org.dmfs.android.microfragments.demo.microfragments.MicroFragment();
        }


        @Override
        public org.dmfs.android.microfragments.demo.microfragments.MicroFragment[] newArray(int size)
        {
            return new org.dmfs.android.microfragments.demo.microfragments.MicroFragment[size];
        }
    };


    /**
     * A simple {@link Fragment} subclass.
     */
    public static class Step1Fragment extends Fragment
    {

        public Step1Fragment()
        {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_final, container, false);
            return view;
        }
    }

}
