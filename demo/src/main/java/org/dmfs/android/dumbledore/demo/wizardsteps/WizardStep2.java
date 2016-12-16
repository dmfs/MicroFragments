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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dmfs.android.dumbledore.WizardStep;
import org.dmfs.android.dumbledore.demo.R;
import org.dmfs.android.dumbledore.transitions.BackWizardTransition;
import org.dmfs.android.dumbledore.transitions.ForwardWizardTransition;

import java.net.URI;


/**
 * Created by marten on 09.12.16.
 */
public final class WizardStep2 implements WizardStep<WizardStep2.Step2Params>
{

    interface Step2Params
    {
        String name();

        URI uri();
    }


    private final Step2Params mParams;


    public WizardStep2(final String name, final URI uri)
    {
        mParams = new Step2Params()
        {

            @Override
            public String name()
            {
                return name;
            }


            @Override
            public URI uri()
            {
                return uri;
            }
        };
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        return "Wizard Step 2";
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context)
    {
        Fragment fragment = new Step2Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WIZARD_STEP, this);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Step2Params parameters()
    {
        return mParams;
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
        dest.writeString(mParams.name());
        dest.writeSerializable(mParams.uri());
    }


    public Creator<WizardStep2> CREATOR = new Creator<WizardStep2>()
    {
        @Override
        public WizardStep2 createFromParcel(Parcel source)
        {
            return new WizardStep2(source.readString(), (URI) source.readSerializable());
        }


        @Override
        public WizardStep2[] newArray(int size)
        {
            return new WizardStep2[size];
        }
    };


    /**
     * A simple {@link Fragment} subclass.
     */
    public static class Step2Fragment extends Fragment implements View.OnClickListener
    {
        private WizardStep<Step2Params> mStep;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mStep = getArguments().getParcelable(WizardStep.ARG_WIZARD_STEP);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_step2, container, false);
            ((TextView) view.findViewById(R.id.text)).setText(mStep.parameters().name() + " " + mStep.parameters().uri().toASCIIString());

            view.findViewById(android.R.id.button1).setOnClickListener(this);
            view.findViewById(android.R.id.button2).setOnClickListener(this);
            return view;
        }


        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case android.R.id.button1:
                    new BackWizardTransition().execute(getActivity());
                    break;
                case android.R.id.button2:
                    new ForwardWizardTransition(new FinalLoaderWizardStep()).execute(getActivity());
                    break;
            }
        }

    }

}
