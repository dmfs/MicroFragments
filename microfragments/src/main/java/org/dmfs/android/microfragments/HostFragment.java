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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.ParcelableDovecote;

import java.util.LinkedList;


/**
 * A {@link Fragment} to host a {@link MicroFragmentFlow}.
 *
 * @author Marten Gajda
 */
public final class HostFragment extends Fragment implements FragmentManager.OnBackStackChangedListener, Dovecote.OnPigeonReturnCallback<FragmentTransition>
{
    private FragmentManager mFragmentManager;
    private int mBackStackDepth = 0;
    private ParcelableDovecote<FragmentTransition> mOperationDoveCote;
    private boolean mInstanceStateSaved;
    private Timestamp mLastTransactionTimestamp = new UiTimestamp();

    /**
     * A queue of {@link FragmentTransition}s that arrive while this Fragment is paused. Note that the queue is no persisted.
     */
    private final LinkedList<FragmentTransition> mTransitionQueue = new LinkedList<>();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mFragmentManager = getChildFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);
        mOperationDoveCote = new ParcelableDovecote<>(getActivity(), "wizardhost", this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View result = new FrameLayout(inflater.getContext());
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        result.setId(R.id.microfragments_host);
        return result;
    }


    @Override
    public void onStart()
    {
        mInstanceStateSaved = false;
        if (currentFragment() == null)
        {
            mFragmentManager.beginTransaction()
                    .add(R.id.microfragments_host,
                            ((MicroFragment<?>) getArguments().getParcelable("INITIAL_MICRO_FRAGMENT")).fragment(getActivity(),
                                    new BasicMicroFragmentHost(mOperationDoveCote.cage())))
                    .commit();
        }
        else
        {
            // replay transitions as early as possible
            replayTransitions();
        }
        super.onStart();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mInstanceStateSaved = false;
        if (!mTransitionQueue.isEmpty())
        {
            // maybe onStart wasn't called because the fragment wasn't stopped, just paused
            replayTransitions();
        }
        postUpdate(new FragmentEnvironment<>(currentFragment()).microFragment());
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        mInstanceStateSaved = true;
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy()
    {
        mOperationDoveCote.dispose();
        super.onDestroy();
    }


    private void replayTransitions()
    {
        // play queued transitions
        while (!mTransitionQueue.isEmpty())
        {
            executeTransition(getActivity(), mTransitionQueue.removeFirst());
        }

        mBackStackDepth = mFragmentManager.getBackStackEntryCount();
    }


    @Override
    public void onBackStackChanged()
    {
        if (mFragmentManager.getBackStackEntryCount() < mBackStackDepth)
        {
            // the user went back, make sure we skip all skipable steps.
            if (mFragmentManager.getBackStackEntryCount() > 0
                    && "skip".equals(mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName()))
            {
                mFragmentManager.popBackStackImmediate("skip", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        mBackStackDepth = mFragmentManager.getBackStackEntryCount();

        postUpdate(new FragmentEnvironment<>(currentFragment()).microFragment());
    }


    private void postUpdate(@NonNull MicroFragment currentStep)
    {
        Cage<MicroFragmentState> cage = getArguments().getParcelable("cage");
        if (cage == null || !isResumed())
        {
            // no pigeon to be send
            return;
        }

        cage.pigeon(new SimpleMicroFragmentState(currentStep, mBackStackDepth)).send(getActivity());
    }


    @Override
    public void onPigeonReturn(@NonNull FragmentTransition fragmentTransition)
    {
        if (mInstanceStateSaved)
        {
            // store the transition for later
            mTransitionQueue.addLast(fragmentTransition);
            return;
        }

        executeTransition(getActivity(), fragmentTransition);
    }


    private void executeTransition(@NonNull Activity activity, @NonNull FragmentTransition fragmentTransition)
    {

        if (!fragmentTransition.timestamp().isAfter(mLastTransactionTimestamp))
        {
            // ignore outdated transition
            return;
        }
        mLastTransactionTimestamp = fragmentTransition.timestamp();

        MicroFragment microFragment = new FragmentEnvironment<>(mFragmentManager.findFragmentById(R.id.microfragments_host)).microFragment();
        MicroFragmentHost host = new BasicMicroFragmentHost(mOperationDoveCote.cage());
        fragmentTransition.prepare(activity, mFragmentManager, host, microFragment);
        FragmentTransaction fragmentTransaction = fragmentTransition.updateTransaction(activity, mFragmentManager.beginTransaction(),
                mFragmentManager, host, microFragment);
        if (!fragmentTransaction.isEmpty())
        {
            fragmentTransaction.commit();
        }
        mFragmentManager.executePendingTransactions();
        fragmentTransition.cleanup(activity, mFragmentManager, host, microFragment);

        // post an update, in case the backstack has not been changed by the transition we still have to notify the listener
        postUpdate(new FragmentEnvironment<>(currentFragment()).microFragment());

        // close keyboard if necessary
        if (activity.getCurrentFocus() == null)
        {
            // no view is focused, close the keyboard
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
        }
    }


    private Fragment currentFragment()
    {
        return mFragmentManager.findFragmentById(R.id.microfragments_host);
    }
}
