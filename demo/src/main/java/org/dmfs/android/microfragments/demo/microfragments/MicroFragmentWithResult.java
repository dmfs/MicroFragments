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
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.demo.R;
import org.dmfs.android.microfragments.transitions.BackWithResultTransition;
import org.dmfs.pigeonpost.Cage;


/**
 * Created by marten on 09.12.16.
 */
public final class MicroFragmentWithResult implements MicroFragment<Cage<MicroFragmentWithResult.ResultType>>
{

    private final Cage<ResultType> mCage;


    public MicroFragmentWithResult(Cage<ResultType> cage)
    {
        mCage = cage;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        return "Step 2";
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull final MicroFragmentHost host)
    {
        return new Step2Fragment();
    }


    @NonNull
    @Override
    public Cage<ResultType> parameter()
    {
        return mCage;
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


    public final static Creator<MicroFragmentWithResult> CREATOR = new Creator<MicroFragmentWithResult>()
    {
        @Override
        public MicroFragmentWithResult createFromParcel(Parcel source)
        {
            return new MicroFragmentWithResult((Cage<ResultType>) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public MicroFragmentWithResult[] newArray(int size)
        {
            return new MicroFragmentWithResult[size];
        }
    };


    /**
     * A simple {@link Fragment} subclass.
     */
    public static class Step2Fragment extends Fragment implements View.OnClickListener
    {
        private MicroFragmentEnvironment<Cage<ResultType>> mEnvironment;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mEnvironment = getArguments().getParcelable(MicroFragment.ARG_ENVIRONMENT);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_with_result, container, false);

            view.findViewById(android.R.id.button1).setOnClickListener(this);
            view.findViewById(android.R.id.button2).setOnClickListener(this);
            return view;
        }


        @Override
        public void onClick(View v)
        {
            MicroFragmentEnvironment<?> environment = new FragmentEnvironment<>(this);
            switch (v.getId())
            {
                case android.R.id.button1:
                    environment.host()
                            .execute(getActivity(), new BackWithResultTransition<>(mEnvironment.microFragment().parameter(), new ResultType("RESULT1")));
                    break;
                case android.R.id.button2:
                    environment.host()
                            .execute(getActivity(), new BackWithResultTransition<>(mEnvironment.microFragment().parameter(), new ResultType("result2")));
                    break;
            }
        }
    }


    public final static class ResultType implements Parcelable
    {
        private final String mValue;


        public ResultType(String value)
        {
            mValue = value;
        }


        @Override
        public int describeContents()
        {
            return 0;
        }


        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeString(mValue);
        }


        @Override
        public String toString()
        {
            return mValue;
        }


        public final static Creator<ResultType> CREATOR = new Creator<ResultType>()
        {
            @Override
            public ResultType createFromParcel(Parcel source)
            {
                return new ResultType(source.readString());
            }


            @Override
            public ResultType[] newArray(int size)
            {
                return new ResultType[size];
            }
        };
    }
}