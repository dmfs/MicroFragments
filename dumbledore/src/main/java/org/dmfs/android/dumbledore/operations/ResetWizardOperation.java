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

package org.dmfs.android.dumbledore.operations;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.dmfs.android.dumbledore.R;
import org.dmfs.android.dumbledore.WizardStep;


/**
 * A {@link WizardOperation} that clears the back stack and starts over with a new {@link WizardStep}.
 *
 * @author Marten Gajda
 */
public final class ResetWizardOperation extends AbstractWizardOperation
{
    private final WizardStep mNextStep;


    /**
     * Creates a {@link WizardOperation} that resets the back stack and starts over with the given {@link WizardStep}.
     *
     * @param nextStep
     *         The initial {@link WizardStep}.
     */
    public ResetWizardOperation(@NonNull WizardStep nextStep)
    {
        this.mNextStep = nextStep;
    }


    @Override
    public void apply(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull WizardStep previousStep)
    {
        // remove everything from the backstack and set a new root fragment
        while (fragmentManager.popBackStackImmediate())
        {
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (useRtl(context))
        {
            transaction.setCustomAnimations(R.anim.wand_wizard_enter_left, R.anim.wand_wizard_exit_right, R.anim.wand_wizard_enter_right,
                    R.anim.wand_wizard_exit_left);
        }
        else
        {
            transaction.setCustomAnimations(R.anim.wand_wizard_enter_right, R.anim.wand_wizard_exit_left, R.anim.wand_wizard_enter_left,
                    R.anim.wand_wizard_exit_right);
        }
        transaction.replace(R.id.dumbledore_wizard_host, mNextStep.fragment(context));
        transaction.commit();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mNextStep, flags);
    }


    public final static Parcelable.Creator<ResetWizardOperation> CREATOR = new Parcelable.Creator<ResetWizardOperation>()
    {
        @Override
        public ResetWizardOperation createFromParcel(Parcel source)
        {
            return new ResetWizardOperation((WizardStep) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public ResetWizardOperation[] newArray(int size)
        {
            return new ResetWizardOperation[size];
        }
    };
}
