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

import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Pigeon;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;


/**
 * A host for {@link MicroFragment}s.
 *
 * @author Marten Gajda
 */
public interface MicroFragmentFlow
{
    /**
     * Set a {@link Cage} that creates {@link Pigeon}s to post the current {@link MicroFragmentState} whenever is changes. Calling this another time will
     * override any previously set {@link Cage}.
     *
     * @param state
     *         The new {@link MicroFragmentState}.
     *
     * @return An updated {@link MicroFragmentFlow}.
     */
    @NonNull
    MicroFragmentFlow withPigeonCage(@NonNull Cage<MicroFragmentState> state);

    /**
     * Show the {@link MicroFragment} in the given {@link FragmentActivity}.
     *
     * @param fragmentActivity
     */
    @NonNull
    MicroFragmentHost start(@NonNull FragmentActivity fragmentActivity);
}
