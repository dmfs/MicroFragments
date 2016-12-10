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
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import org.dmfs.android.dumbledore.WizardStep;


/**
 * Represents an operation that switches from one WizardStep to another one.
 *
 * @author Marten Gajda
 */
public interface WizardOperation extends Parcelable
{
    /**
     * Applies this transition to the given FragmentManager.
     *
     * @param context
     *         A Context.
     * @param fragmentManager
     *         The FragmentManager on which to perform the transition.
     * @param previousStep
     *         The previous WizardStep.
     */
    void apply(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull WizardStep previousStep);
}
