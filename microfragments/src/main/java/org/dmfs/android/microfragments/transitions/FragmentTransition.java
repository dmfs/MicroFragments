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

package org.dmfs.android.microfragments.transitions;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;


/**
 * Represents an operation that switches from one {@link MicroFragment} to another one.
 *
 * @author Marten Gajda
 */
public interface FragmentTransition extends Parcelable
{
    /**
     * The {@link Timestamp} of the origin of this {@link FragmentTransition}.
     * <p>
     * For background processes this is usually the time of when the background process has been started.
     *
     * @return
     */
    @NonNull
    Timestamp timestamp();

    void prepare(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep);

    /**
     * Applies this transition to the given FragmentManager.
     *
     * @param context
     *         A Context.
     * @param fragmentTransaction
     * @param fragmentManager
     * @param previousStep
     *
     * @return A {@link FragmentTransaction} that performs this transition.
     */
    @NonNull
    FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep);

    void cleanup(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep);
}
