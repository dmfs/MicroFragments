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

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import org.dmfs.android.pigeonpost.Cage;
import org.dmfs.android.pigeonpost.Pigeon;


/**
 * A Wizard.
 *
 * @author Marten Gajda
 */
public interface Wizard
{
    /**
     * Set a {@link Cage} that creates {@link Pigeon}s to post the current {@link WizardState} whenever is changes. Calling this another time will override any
     * previously set {@link Cage}.
     *
     * @param state
     *         The new {@link WizardState}.
     *
     * @return An updated {@link Wizard}.
     */
    @NonNull
    Wizard withPigeonCage(@NonNull Cage<WizardState> state);

    /**
     * Start the Wizard in the given {@link FragmentActivity}.
     *
     * @param fragmentActivity
     */
    void start(@NonNull FragmentActivity fragmentActivity);
}
