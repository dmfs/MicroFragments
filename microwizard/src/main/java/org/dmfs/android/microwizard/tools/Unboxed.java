/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.android.microwizard.tools;

import android.os.Parcel;
import android.os.Parcelable;

import org.dmfs.jems.single.Single;


/**
 * @author Marten Gajda
 *
 * TODO: move to bolts
 */
public final class Unboxed<T> implements Single<T>
{

    private final Parcelable mParcelable;


    public Unboxed(Parcel parcel)
    {
        // note this needs to be evaluated eagerly because we need to read the parcel at the current position
        this((Parcelable) parcel.readParcelable(Unboxed.class.getClassLoader()));
    }


    public Unboxed(Parcelable parcelable)
    {
        mParcelable = parcelable;
    }


    @Override
    public T value()
    {
        return ((Box<T>) mParcelable).value();
    }
}
