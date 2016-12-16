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

package org.dmfs.android.dumbledore.demo.wizardsteps;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dmfs.android.dumbledore.WizardStep;
import org.dmfs.android.dumbledore.demo.R;
import org.dmfs.android.dumbledore.transitions.ForwardWizardTransition;


/**
 * Created by marten on 09.12.16.
 */

public final class WizardStep1 implements WizardStep<String>
{

    private final String mName;


    public WizardStep1(String name)
    {
        mName = name;
    }


    @Override
    public String title(Context context)
    {
        return mName;
    }


    @Override
    public Fragment fragment(Context context)
    {
        Fragment fragment = new Step1Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WIZARD_STEP, this);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public String parameters()
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


    public Creator<WizardStep1> CREATOR = new Creator<WizardStep1>()
    {
        @Override
        public WizardStep1 createFromParcel(Parcel source)
        {
            return new WizardStep1(source.readString());
        }


        @Override
        public WizardStep1[] newArray(int size)
        {
            return new WizardStep1[size];
        }
    };


    /**
     * A simple {@link Fragment} subclass.
     */
    public static class Step1Fragment extends Fragment implements View.OnClickListener
    {

        private WizardStep<String> mWizardStep;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mWizardStep = getArguments().getParcelable(WizardStep.ARG_WIZARD_STEP);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_step1, container, false);

            ((TextView) view.findViewById(R.id.text)).setText(getActivity().getString(R.string.hello_fragment, mWizardStep.parameters()));
            view.findViewById(android.R.id.button1).setOnClickListener(this);
            return view;
        }


        @Override
        public void onClick(View v)
        {
            new ForwardWizardTransition(new IntermediateLoaderWizardStep()).execute(getActivity());
        }
    }

}
