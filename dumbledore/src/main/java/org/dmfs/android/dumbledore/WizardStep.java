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

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


/**
 * Interface of a specific step of the wizard.
 */
public interface WizardStep<T> extends Parcelable
{
    /**
     * The Fragment argument that contains the WizardStep that a Fragment belongs to. This must be set on every wizard fragment.
     */
    String ARG_WIZARD_STEP = "WIZARD_STEP";

    /**
     * Returns the title of this step.
     *
     * @param context
     *         A {@link Context}.
     *
     * @return The localized wizard step title.
     */
    @NonNull
    String title(@NonNull Context context);

    /**
     * Creates the Fragment that represents this WizardStep.
     *
     * @param context
     *         A {@link Context}.
     *
     * @return A {@link Fragment}.
     */
    @NonNull
    Fragment fragment(@NonNull Context context);

    /**
     * The parameters of this step.
     *
     * @return The parameter object.
     */
    T parameters();

    /**
     * True if this step should be skipped when going back to the previous step.
     *
     * @return
     */
    boolean skipOnBack();

}
