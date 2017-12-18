/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.android.microfragments.demo.steps;

import android.content.Context;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.demo.microfragments.MicroFragment1;
import org.dmfs.android.microwizard.tools.Box;
import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.android.microwizard.tools.AbstractWizardStepBox;


/**
 * @author Marten Gajda
 */
public final class InitialStep implements MicroWizard<Void>
{
    private final MicroWizard<Integer> mNextStep;


    public InitialStep(MicroWizard<Integer> nextStep)
    {
        mNextStep = nextStep;
    }


    @Override
    public MicroFragment<MicroFragment1.Params> microFragment(Context context, Void params)
    {
        return new MicroFragment1("Step1", mNextStep);
    }


    @Override
    public Box<MicroWizard<Void>> boxed()
    {
        return new WizardStepBox(mNextStep);
    }


    private final static class WizardStepBox extends AbstractWizardStepBox<Void, Integer>
    {
        WizardStepBox(MicroWizard<Integer> next)
        {
            super(next, InitialStep::new);
        }


        public final static Creator<MicroWizard<Void>> CREATOR = new WizardStepCreator<>(InitialStep::new, InitialStep[]::new);
    }
}
