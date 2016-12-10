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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import org.dmfs.android.dumbledore.operations.WizardOperation;
import org.dmfs.android.pigeonpost.Cage;

import static org.dmfs.android.dumbledore.transitions.AbstractWizardTransition.ACTION_WIZARD_OPERATION;
import static org.dmfs.android.dumbledore.transitions.AbstractWizardTransition.EXTRA_WIZARD_OPERATION;


/**
 * A Fragment to host a {@link Wizard}.
 *
 * @author Marten Gajda
 */
public final class WizardHost extends Fragment implements FragmentManager.OnBackStackChangedListener
{
    private final Handler mHandler = new Handler();
    private FragmentManager mFragmentManager;
    private int mBackStackDepth = 0;


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
        View view = new FrameLayout(inflater.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setId(R.id.dumbledore_wizard_host);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null)
        {
            mFragmentManager.beginTransaction()
                    .add(R.id.dumbledore_wizard_host, ((WizardStep) getArguments().getParcelable(WizardStep.ARG_WIZARD_STEP)).fragment(getActivity()))
                    .commit();
        }
        else
        {
            mBackStackDepth = mFragmentManager.getBackStackEntryCount();
            postUpdate((WizardStep) mFragmentManager.findFragmentById(R.id.dumbledore_wizard_host).getArguments().getParcelable(WizardStep.ARG_WIZARD_STEP));
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        // set up wizard controller
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_WIZARD_OPERATION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mWizardTransactionReceiver, filter);
    }


    @Override
    public void onPause()
    {
        // clear the broadcast receiver, to make sure we don't perform any fragment transactions after onSaveInstanceState, we do this in onPause.
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mWizardTransactionReceiver);
        super.onPause();
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

        postUpdate((WizardStep) mFragmentManager.findFragmentById(R.id.dumbledore_wizard_host).getArguments().getParcelable(WizardStep.ARG_WIZARD_STEP));
    }


    private void postUpdate(WizardStep currentStep)
    {
        Cage<WizardState> cage = getArguments().getParcelable("cage");
        if (cage == null)
        {
            // no pigeon to be send
            return;
        }

        cage.pigeon(new SimpleWizardState(currentStep, mBackStackDepth)).send(getActivity());
    }


    private final BroadcastReceiver mWizardTransactionReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (ACTION_WIZARD_OPERATION.equals(intent.getAction()))
                    {
                        WizardOperation wizardTransition = intent.getParcelableExtra(EXTRA_WIZARD_OPERATION);
                        WizardStep wizardStep = mFragmentManager.findFragmentById(R.id.dumbledore_wizard_host)
                                .getArguments()
                                .getParcelable(WizardStep.ARG_WIZARD_STEP);
                        wizardTransition.apply(getActivity(), mFragmentManager, wizardStep);
                        mFragmentManager.executePendingTransactions();

                        // close keyboard if necessary
                        View view = getActivity().getCurrentFocus();
                        if (view == null)
                        {
                            // no view is focused, close the keyboard
                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
                        }

                        // postUpdate(wizardStep);
                    }
                }
            });
        }
    };

}
