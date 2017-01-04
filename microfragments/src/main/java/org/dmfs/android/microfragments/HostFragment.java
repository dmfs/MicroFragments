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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.ParcelableDovecote;


/**
 * A {@link Fragment} to host a {@link MicroFragmentFlow}.
 *
 * @author Marten Gajda
 */
public final class HostFragment extends Fragment implements FragmentManager.OnBackStackChangedListener, Dovecote.OnPigeonReturnCallback<FragmentTransition>, View.OnKeyListener
{
    private FragmentManager mFragmentManager;
    private int mBackStackDepth = 0;
    private ParcelableDovecote<FragmentTransition> mOperationDoveCote;
    private boolean mInstanceStateSaved;
    private Timestamp mLastTransactionTimestamp = new UiTimestamp();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mFragmentManager = getChildFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View result = new FrameLayout(inflater.getContext());
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        result.setId(R.id.microfragments_host);
        result.setOnKeyListener(this);
        // make sure we receive the back button event
        result.setFocusableInTouchMode(true);
        return result;
    }


    @Override
    public void onStart()
    {
        mInstanceStateSaved = false;
        mOperationDoveCote = new ParcelableDovecote<>(getActivity(), "wizardhost", this);
        if (currentFragment() == null)
        {
            mFragmentManager.beginTransaction()
                    .add(R.id.microfragments_host,
                            ((MicroFragment) getArguments().getParcelable(MicroFragment.ARG_MICRO_FRAGMENT)).fragment(getActivity(),
                                    new BasicMicroFragmentHost(mOperationDoveCote.cage())))
                    .commit();
        }
        else
        {
            mBackStackDepth = mFragmentManager.getBackStackEntryCount();
            postUpdate(
                    (MicroFragment) currentFragment()
                            .getArguments()
                            .getParcelable(MicroFragment.ARG_MICRO_FRAGMENT));
            //        currentFragment().onStart();
        }
        super.onStart();
    }


    @Override
    public void onStop()
    {
        mOperationDoveCote.dispose();
        super.onStop();
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        mInstanceStateSaved = true;
        super.onSaveInstanceState(outState);
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

        postUpdate(
                (MicroFragment) currentFragment().getArguments().getParcelable(MicroFragment.ARG_MICRO_FRAGMENT));
    }


    private void postUpdate(MicroFragment currentStep)
    {
        Cage<MicroFragmentState> cage = getArguments().getParcelable("cage");
        if (cage == null || !isAdded())
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
            // ignore pigeons that return after saving the instance state
            return;
        }

        if (!fragmentTransition.timestamp().isAfter(mLastTransactionTimestamp))
        {
            // ignore outdated transition
            Log.i("HostFragment", "outdated transition ignored");
            return;
        }
        mLastTransactionTimestamp = fragmentTransition.timestamp();

        MicroFragment microFragment = mFragmentManager.findFragmentById(R.id.microfragments_host)
                .getArguments()
                .getParcelable(MicroFragment.ARG_MICRO_FRAGMENT);
        MicroFragmentHost host = new BasicMicroFragmentHost(mOperationDoveCote.cage());
        fragmentTransition.prepare(getActivity(), mFragmentManager, host, microFragment);
        FragmentTransaction fragmentTransaction = fragmentTransition.updateTransaction(getActivity(), mFragmentManager.beginTransaction(),
                mFragmentManager, host, microFragment);
        if (!fragmentTransaction.isEmpty())
        {
            fragmentTransaction.commit();
        }
        mFragmentManager.executePendingTransactions();
        fragmentTransition.cleanup(getActivity(), mFragmentManager, host, microFragment);

        // close keyboard if necessary
        View view = getActivity().getCurrentFocus();
        if (view == null)
        {
            // no view is focused, close the keyboard
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
        }
    }


    private Fragment currentFragment()
    {
        return mFragmentManager.findFragmentById(R.id.microfragments_host);
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent)
    {
        // nested Fragments don't respond to the back key, so we have to do that manually
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && mFragmentManager.getBackStackEntryCount() > 0)
        {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP)
            {
                mFragmentManager.popBackStackImmediate();
            }
            return true;
        }
        return false;
    }
}
