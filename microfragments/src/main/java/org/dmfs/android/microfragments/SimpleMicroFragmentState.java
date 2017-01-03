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

import android.os.Parcel;
import android.support.annotation.NonNull;


/**
 * The state of a wizard.
 *
 * @author Marten Gajda
 */
public final class SimpleMicroFragmentState implements MicroFragmentState
{
    private final MicroFragment mCurrentStep;
    private final int mNum;


    public SimpleMicroFragmentState(@NonNull MicroFragment currentStep, int num)
    {
        mCurrentStep = currentStep;
        mNum = num;
    }


    @NonNull
    @Override
    public MicroFragment currentStep()
    {
        return mCurrentStep;
    }


    @Override
    public int backStackSize()
    {
        return mNum;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mCurrentStep, flags);
        dest.writeInt(mNum);
    }


    public final static Creator<SimpleMicroFragmentState> CREATOR = new Creator<SimpleMicroFragmentState>()
    {
        @Override
        public SimpleMicroFragmentState createFromParcel(Parcel source)
        {
            return new SimpleMicroFragmentState((MicroFragment) source.readParcelable(getClass().getClassLoader()), source.readInt());
        }


        @Override
        public SimpleMicroFragmentState[] newArray(int size)
        {
            return new SimpleMicroFragmentState[size];
        }
    };
}
