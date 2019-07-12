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

import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.demo.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * Created by marten on 09.12.16.
 */
public final class LastMicroFragment implements org.dmfs.android.microfragments.MicroFragment<Void>
{
    private final String mName;


    public LastMicroFragment(String name)
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
    public Void parameter()
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
        dest.writeString(mName);
    }


    public final static Creator<LastMicroFragment> CREATOR = new Creator<LastMicroFragment>()
    {
        @Override
        public LastMicroFragment createFromParcel(Parcel source)
        {
            return new LastMicroFragment(source.readString());
        }


        @Override
        public LastMicroFragment[] newArray(int size)
        {
            return new LastMicroFragment[size];
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
