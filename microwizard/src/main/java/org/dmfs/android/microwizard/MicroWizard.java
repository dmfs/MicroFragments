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

package org.dmfs.android.microwizard;

import android.content.Context;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microwizard.tools.Boxable;


/**
 * A single step of a MicroWizard.
 *
 * @author Marten Gajda
 */
public interface MicroWizard<V> extends Boxable<MicroWizard<V>>
{
    /**
     * Returns the {@link MicroFragment} of this {@link MicroWizard}.
     *
     * @param context
     *         A {@link Context}.
     * @param params
     *         Any parameters for this step.
     *
     * @return A {@link MicroFragment} for this step of the wizard.
     */
    MicroFragment<?> microFragment(Context context, V params);
}
