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

package org.dmfs.android.microfragments;

import android.os.Parcelable;
import android.support.annotation.NonNull;


/**
 * Represents the current state of a {@link MicroFragment}.
 *
 * @author Marten Gajda
 */
public interface MicroFragmentState extends Parcelable
{
    /**
     * Returns the step that the {@link HostFragment} currently shows.
     *
     * @return The current {@link MicroFragment}.
     */
    @NonNull
    MicroFragment currentStep();

    /**
     * The size of the backstack. It represents the number of previous steps. This will be {@code 0} for the initial {@link MicroFragment}.
     *
     * @return The number of previous {@link MicroFragment}s on the backstack.
     */
    int backStackSize();
}
