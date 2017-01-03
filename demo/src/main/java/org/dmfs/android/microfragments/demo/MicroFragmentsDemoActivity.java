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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.dmfs.android.microfragments.MicroFragmentState;
import org.dmfs.android.microfragments.SimpleMicroFragmentFlow;
import org.dmfs.android.microfragments.demo.microfragments.MicroFragment1;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.ParcelableDovecote;


public final class MicroFragmentsDemoActivity extends AppCompatActivity implements Dovecote.OnPigeonReturnCallback<MicroFragmentState>
{
    private Dovecote<MicroFragmentState> mDovecote;

    private boolean mLeaveOnBack = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microfragments_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDovecote = new ParcelableDovecote<>(this, "microfragments", this);

        if (savedInstanceState == null)
        {
            new SimpleMicroFragmentFlow(new MicroFragment1("Step1"), R.id.wizard_host).withPigeonCage(mDovecote.cage()).start(this);
        }
    }


    @Override
    protected void onDestroy()
    {
        mDovecote.dispose();
        super.onDestroy();
    }


    @Override
    public void onPigeonReturn(@NonNull MicroFragmentState payload)
    {
        getSupportActionBar().setTitle(payload.currentStep().title(this));
        mLeaveOnBack = payload.backStackSize() == 0;
    }

}
