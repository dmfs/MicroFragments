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

package org.dmfs.android.microfragments.demo;

import android.os.Bundle;

import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.MicroFragmentState;
import org.dmfs.android.microfragments.SimpleMicroFragmentFlow;
import org.dmfs.android.microfragments.demo.steps.FinalLoaderStep;
import org.dmfs.android.microfragments.demo.steps.FinalStep;
import org.dmfs.android.microfragments.demo.steps.InitialStep;
import org.dmfs.android.microfragments.demo.steps.LoaderStep1;
import org.dmfs.android.microfragments.demo.steps.Step2;
import org.dmfs.android.microfragments.transitions.BackTransition;
import org.dmfs.android.microfragments.utils.BooleanDovecote;
import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.ParcelableDovecote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public final class MicroFragmentsDemoActivity extends AppCompatActivity implements Dovecote.OnPigeonReturnCallback<MicroFragmentState>
{
    private Dovecote<MicroFragmentState> mDovecote;
    private Dovecote<Boolean> mBackDovecote;
    private MicroFragmentHost mMicroFragmentHost;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microfragments_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDovecote = new ParcelableDovecote<>(this, "microfragments", this);
        mBackDovecote = new BooleanDovecote(this, "backresult", new Dovecote.OnPigeonReturnCallback<Boolean>()
        {
            @Override
            public void onPigeonReturn(@NonNull Boolean aBoolean)
            {
                if (!aBoolean)
                {
                    finish();
                }
            }
        });

        if (savedInstanceState == null)
        {
            // Declare the microWizard.
            MicroWizard<?> microWizard = new InitialStep(new LoaderStep1(new Step2(new FinalLoaderStep(new FinalStep()))));

            mMicroFragmentHost = new SimpleMicroFragmentFlow(
                    microWizard.microFragment(this, null),
                    R.id.wizard_host).withPigeonCage(
                    mDovecote.cage()).start(this);
        }
        else
        {
            mMicroFragmentHost = savedInstanceState.getParcelable("host");
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable("host", mMicroFragmentHost);
    }


    @Override
    protected void onDestroy()
    {
        mDovecote.dispose();
        mBackDovecote.dispose();
        super.onDestroy();
    }


    @Override
    public void onPigeonReturn(@NonNull MicroFragmentState payload)
    {
        getSupportActionBar().setTitle(payload.currentStep().title(this));
    }


    @Override
    public void onBackPressed()
    {
        mMicroFragmentHost.execute(this, new BackTransition(mBackDovecote.cage()));
    }
}
