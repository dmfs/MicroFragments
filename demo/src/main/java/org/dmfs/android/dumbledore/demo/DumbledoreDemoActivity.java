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

package org.dmfs.android.dumbledore.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.dmfs.android.dumbledore.SimpleWizard;
import org.dmfs.android.dumbledore.WizardState;
import org.dmfs.android.dumbledore.demo.dovecote.LbDovecote;
import org.dmfs.android.dumbledore.demo.wizardsteps.WizardStep1;
import org.dmfs.android.dumbledore.transitions.BackWizardTransition;
import org.dmfs.android.pigeonpost.Dovecote;


public final class DumbledoreDemoActivity extends AppCompatActivity implements Dovecote.OnPigeonReturnCallback<WizardState>
{
    private Dovecote<WizardState> mDovecote;

    private boolean mLeaveOnBack = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumbledore_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDovecote = new LbDovecote<>(this, "wizard-line", this);

        if (savedInstanceState == null)
        {
            new SimpleWizard(new WizardStep1(), R.id.wizard_host).withPigeonCage(mDovecote.cage()).start(this);
        }
    }


    @Override
    protected void onDestroy()
    {
        mDovecote.dispose();
        super.onDestroy();
    }


    @Override
    public void onPigeonReturn(WizardState payload)
    {
        getSupportActionBar().setTitle(payload.currentStep().title(this));
        mLeaveOnBack = payload.backStackSize() == 0;
    }


    @Override
    public void onBackPressed()
    {
        if (mLeaveOnBack)
        {
            super.onBackPressed();
        }
        else
        {
            new BackWizardTransition().execute(this);
        }
    }
}
