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
import org.dmfs.android.microfragments.demo.microfragments.IntermediateLoaderMicroFragment;
import org.dmfs.android.microwizard.tools.Box;
import org.dmfs.android.microwizard.MicroWizard;
import org.dmfs.android.microwizard.tools.AbstractFollowUpMicroWizardBox;

import java.net.URI;


/**
 * @author Marten Gajda
 */
public final class LoaderStep1 implements MicroWizard<Integer>
{
    private final MicroWizard<URI> mNextStep;


    public LoaderStep1(MicroWizard<URI> nextStep)
    {
        mNextStep = nextStep;
    }


    @Override
    public MicroFragment<?> microFragment(Context context, Integer params)
    {
        return new IntermediateLoaderMicroFragment(params, mNextStep);
    }


    @Override
    public Box<MicroWizard<Integer>> boxed()
    {
        return new FollowUpMicroWizardBox(mNextStep);
    }


    private final static class FollowUpMicroWizardBox extends AbstractFollowUpMicroWizardBox<Integer, URI>
    {

        FollowUpMicroWizardBox(MicroWizard<URI> next)
        {
            super(next, LoaderStep1::new);
        }


        public final static Creator<MicroWizard<Integer>> CREATOR = new FollowUpMicroWizardCreator<>(LoaderStep1::new, LoaderStep1[]::new);
    }
}
