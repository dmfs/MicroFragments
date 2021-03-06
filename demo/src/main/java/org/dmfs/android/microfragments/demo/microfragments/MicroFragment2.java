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
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.demo.R;
import org.dmfs.android.microfragments.transitions.BackTransition;
import org.dmfs.android.microfragments.transitions.BottomUp;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.OverlayTransition;
import org.dmfs.android.microfragments.transitions.Swiped;
import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.android.microwizard.box.Unboxed;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.ParcelableDovecote;

import java.net.URI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Created by marten on 09.12.16.
 */
public final class MicroFragment2 implements MicroFragment<MicroFragment2.Step2Params>
{

    interface Step2Params
    {
        String name();

        URI uri();

        MicroWizard<URI> next();
    }


    private final Step2Params mParams;


    public MicroFragment2(final String name, final URI uri, final MicroWizard<URI> next)
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


            @Override
            public MicroWizard<URI> next()
            {
                return next;
            }
        };
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
    public Step2Params parameter()
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
        dest.writeParcelable(mParams.next().boxed(), flags);
    }


    public final static Creator<MicroFragment2> CREATOR = new Creator<MicroFragment2>()
    {
        @Override
        public MicroFragment2 createFromParcel(Parcel source)
        {
            return new MicroFragment2(source.readString(), (URI) source.readSerializable(), new Unboxed<MicroWizard<URI>>(source).value());
        }


        @Override
        public MicroFragment2[] newArray(int size)
        {
            return new MicroFragment2[size];
        }
    };


    /**
     * A simple {@link Fragment} subclass.
     */
    public static class Step2Fragment extends Fragment implements View.OnClickListener, Dovecote.OnPigeonReturnCallback<MicroFragmentWithResult.ResultType>
    {
        private MicroFragmentEnvironment<Step2Params> mEnvironment;
        private Dovecote<MicroFragmentWithResult.ResultType> mDoveCote;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mEnvironment = new FragmentEnvironment<>(this);
            mDoveCote = new ParcelableDovecote<>(getActivity(), "step2result", this);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_step2, container, false);
            ((TextView) view.findViewById(R.id.text)).setText(
                    mEnvironment.microFragment().parameter().name() + " " + mEnvironment.microFragment().parameter().uri().toASCIIString());

            view.findViewById(android.R.id.button1).setOnClickListener(this);
            view.findViewById(android.R.id.button2).setOnClickListener(this);
            view.findViewById(android.R.id.button3).setOnClickListener(this);
            return view;
        }


        @Override
        public void onDestroy()
        {
            mDoveCote.dispose();
            super.onDestroy();
        }


        @Override
        public void onClick(View v)
        {
            MicroFragmentEnvironment<Step2Params> environment = getArguments().getParcelable(MicroFragment.ARG_ENVIRONMENT);
            switch (v.getId())
            {
                case android.R.id.button1:
                    environment.host().execute(getActivity(), new BackTransition());
                    break;
                case android.R.id.button2:
                    environment.host()
                            .execute(getActivity(),
                                    new Swiped(
                                            new ForwardTransition<>(environment.microFragment()
                                                    .parameter()
                                                    .next()
                                                    .microFragment(getActivity(), mEnvironment.microFragment().parameter().uri()))));
                    break;
                case android.R.id.button3:
                    environment.host().execute(getActivity(), new BottomUp(new OverlayTransition<>(new MicroFragmentWithResult(mDoveCote.cage()))));
                    break;
            }
        }


        @Override
        public void onPigeonReturn(@NonNull MicroFragmentWithResult.ResultType result)
        {
            if (result != null)
            {
                Snackbar.make(getView(), String.format("Result was %s", result.toString()), Snackbar.LENGTH_LONG).show();
            }
        }
    }
}