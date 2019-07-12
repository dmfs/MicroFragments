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

import android.os.Bundle;

import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.ParcelableDovecote;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;


/**
 * A simple {@link MicroFragmentFlow}.
 *
 * @author Marten Gajda
 */
public final class SimpleMicroFragmentFlow implements MicroFragmentFlow
{
    private final Bundle mArguments;
    @IdRes
    private final int mContainerId;


    /**
     * Create a simple {@link MicroFragmentFlow} with the given initial {@link MicroFragment} in the view having the given hostId.
     *
     * @param microFragment
     *         The {@link MicroFragment} to begin with.
     * @param containerId
     *         The container view holding the {@link MicroFragmentFlow}
     */
    public SimpleMicroFragmentFlow(@NonNull MicroFragment microFragment, @IdRes int containerId)
    {
        this(arguments(microFragment), containerId);
    }


    private SimpleMicroFragmentFlow(@NonNull Bundle arguments, @IdRes int containerId)
    {
        mArguments = arguments;
        mContainerId = containerId;
    }


    @NonNull
    @Override
    public MicroFragmentFlow withPigeonCage(@NonNull Cage<MicroFragmentState> cage)
    {
        Bundle newArgs = new Bundle(mArguments);
        newArgs.putParcelable("cage", cage);
        return new SimpleMicroFragmentFlow(newArgs, mContainerId);
    }


    @NonNull
    @Override
    public MicroFragmentHost start(@NonNull FragmentActivity fragmentActivity)
    {
        HostFragment hostFragment = new HostFragment();
        hostFragment.setArguments(mArguments);
        fragmentActivity.getSupportFragmentManager().beginTransaction().add(mContainerId, hostFragment).commit();

        // TODO: refactor this to get rid of code duplication with HostFragment
        Dovecote<FragmentTransition> dovecote = new ParcelableDovecote<>(fragmentActivity, "wizardhost", null);
        Cage<FragmentTransition> cage = dovecote.cage();
        dovecote.dispose();
        return new BasicMicroFragmentHost(cage);
    }


    @NonNull
    private static Bundle arguments(@NonNull MicroFragment microFragment)
    {
        Bundle args = new Bundle(1);
        args.putParcelable("INITIAL_MICRO_FRAGMENT", microFragment);
        return args;
    }
}
