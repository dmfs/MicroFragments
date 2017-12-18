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
import org.dmfs.android.microfragments.demo.microfragments.MicroFragment2;
import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.android.microwizard.box.AbstractSingleBox;
import org.dmfs.android.microwizard.box.Box;

import java.net.URI;


/**
 * @author Marten Gajda
 */
public final class Step2 implements MicroWizard<URI>
{
    private final MicroWizard<URI> mNextStep;


    public Step2(MicroWizard<URI> nextStep)
    {
        mNextStep = nextStep;
    }


    @Override
    public MicroFragment<?> microFragment(Context context, URI params)
    {
        return new MicroFragment2("Step 2", params, mNextStep);
    }


    @Override
    public Box<MicroWizard<URI>> boxed()
    {
        return new WizardBox(mNextStep);
    }


    private final static class WizardBox extends AbstractSingleBox<MicroWizard<URI>, MicroWizard<URI>>
    {

        WizardBox(MicroWizard<URI> next)
        {
            super(next, Step2::new);
        }


        public final static Creator<Box<MicroWizard<URI>>> CREATOR = new SingleBoxableBoxCreator<>(WizardBox::new, WizardBox[]::new);
    }
}
