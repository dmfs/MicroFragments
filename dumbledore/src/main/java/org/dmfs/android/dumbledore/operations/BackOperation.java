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

package org.dmfs.android.dumbledore.operations;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import org.dmfs.android.dumbledore.WizardStep;


/**
 * A {@link WizardOperation} that returns to the previous {@link WizardStep}.
 *
 * @author Marten Gajda
 */
public final class BackOperation implements WizardOperation
{

    /**
     * Creates a {@link WizardOperation} that goes back to the previous {@link WizardStep}.
     */
    public BackOperation()
    {
    }


    @Override
    public void apply(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull WizardStep previousStep)
    {
        fragmentManager.popBackStackImmediate();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // nothing to write here
    }


    public final static Parcelable.Creator<BackOperation> CREATOR = new Parcelable.Creator<BackOperation>()
    {
        @Override
        public BackOperation createFromParcel(Parcel source)
        {
            return new BackOperation();
        }


        @Override
        public BackOperation[] newArray(int size)
        {
            return new BackOperation[size];
        }
    };
}
