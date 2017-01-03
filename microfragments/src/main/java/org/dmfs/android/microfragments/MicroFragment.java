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

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


/**
 * Interface of a micro fragment.
 */
public interface MicroFragment<T> extends Parcelable
{
    /**
     * The Fragment argument that contains the {@link MicroFragment} that an actual {@link Fragment} belongs to. Every {@link Fragment} that belongs to a {@link
     * MicroFragment} must have this set.
     */
    String ARG_MICRO_FRAGMENT = "MICRO_FRAGMENT";

    /**
     * Returns the title of this {@link MicroFragment}.
     *
     * @param context
     *         A {@link Context}.
     *
     * @return The localized {@link MicroFragment} title.
     */
    @NonNull
    String title(@NonNull Context context);

    /**
     * Creates the actual Fragment that represents this {@link MicroFragment}.
     *
     * @param context
     *         A {@link Context}.
     * @param host
     *         The {@link MicroFragmentHost} to host this {@link MicroFragment}.
     *
     * @return A {@link Fragment}.
     */
    @NonNull
    Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host);

    /**
     * The parameters of this {@link MicroFragment}.
     *
     * @return The parameter object.
     */
    T parameters();

    /**
     * True if this {@link MicroFragment} should be skipped when going back to the previous {@link MicroFragment}.
     *
     * @return
     */
    boolean skipOnBack();

}
