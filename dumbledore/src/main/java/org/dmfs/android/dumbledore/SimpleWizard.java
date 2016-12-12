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

package org.dmfs.android.dumbledore;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import org.dmfs.pigeonpost.Cage;


/**
 * A simple wizard.
 *
 * @author Marten Gajda
 */
public final class SimpleWizard implements Wizard
{
    private final Bundle mArguments;
    @IdRes
    private final int mHostId;


    /**
     * Create a simple {@link Wizard} with the given initial {@link WizardStep} in the view having the given hostId.
     *
     * @param initialStep
     *         The {@link WizardStep} to begin with.
     * @param hostId
     *         The id of the fragment container view.
     */
    public SimpleWizard(@NonNull WizardStep initialStep, @IdRes int hostId)
    {
        this(arguments(initialStep), hostId);
    }


    private SimpleWizard(@NonNull Bundle arguments, @IdRes int hostId)
    {
        mArguments = arguments;
        mHostId = hostId;
    }


    @NonNull
    @Override
    public Wizard withPigeonCage(@NonNull Cage<WizardState> cage)
    {
        Bundle newArgs = new Bundle(mArguments);
        newArgs.putParcelable("cage", cage);
        return new SimpleWizard(newArgs, mHostId);
    }


    @Override
    public void start(@NonNull FragmentActivity fragmentActivity)
    {
        WizardHost wizardHost = new WizardHost();
        wizardHost.setArguments(mArguments);
        fragmentActivity.getSupportFragmentManager().beginTransaction().add(mHostId, wizardHost).commit();
    }


    private static Bundle arguments(@NonNull WizardStep initialStep)
    {
        Bundle args = new Bundle(1);
        args.putParcelable(WizardStep.ARG_WIZARD_STEP, initialStep);
        return args;
    }
}
