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

package org.dmfs.android.dumbledore.transitions;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import org.dmfs.android.dumbledore.operations.WizardOperation;


/**
 * An abstract {@link WizardTransition} that is executed by applying a given {@link WizardOperation}.
 *
 * @author Marten Gajda
 */
public abstract class AbstractWizardTransition implements WizardTransition
{
    /**
     * The broadcast action that's send.
     */
    public final static String ACTION_WIZARD_OPERATION = "org.dmfs.wizard.action.OPERATION";

    /**
     * The name of the Parcelable extra that contains the {@link WizardOperation}.
     */
    public final static String EXTRA_WIZARD_OPERATION = "wizard-operation";

    private final WizardOperation mOperation;


    protected AbstractWizardTransition(@NonNull WizardOperation operation)
    {
        mOperation = operation;
    }


    @Override
    public final void execute(@NonNull Context context)
    {
        Intent intent = new Intent(ACTION_WIZARD_OPERATION);
        intent.putExtra(EXTRA_WIZARD_OPERATION, mOperation);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
