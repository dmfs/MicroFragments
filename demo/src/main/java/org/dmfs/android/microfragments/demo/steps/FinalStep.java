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
import org.dmfs.android.microfragments.demo.microfragments.LastMicroFragment;
import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.android.microwizard.box.Box;
import org.dmfs.android.microwizard.box.FactoryBox;


/**
 * @author Marten Gajda
 */
public final class FinalStep implements MicroWizard<String>
{

    public FinalStep()
    {
    }


    @Override
    public MicroFragment<?> microFragment(Context context, String params)
    {
        return new LastMicroFragment(params);
    }


    @Override
    public Box<MicroWizard<String>> boxed()
    {
        return new WizardStepBox();
    }


    private final static class WizardStepBox extends FactoryBox<MicroWizard<String>>
    {

        public WizardStepBox()
        {
            super(FinalStep::new);
        }


        public final static Creator<WizardStepBox> CREATOR = new FactoryBoxCreator<>(WizardStepBox::new, WizardStepBox[]::new);
    }
}
