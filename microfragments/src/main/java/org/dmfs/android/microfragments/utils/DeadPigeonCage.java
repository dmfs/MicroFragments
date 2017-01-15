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

package org.dmfs.android.microfragments.utils;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Pigeon;


/**
 * A {@link Cage} that contains {@link Pigeon}s that don't return.
 * <p>
 * TODO: move to PigeonPost project
 *
 * @author Marten Gajda
 */
public final class DeadPigeonCage<T> implements Cage<T>
{
    private final static DeadPigeonCage<?> INSTANCE = new DeadPigeonCage<>();


    /**
     * Return an instance of a {@link DeadPigeonCage}.
     *
     * @param <T>
     *         The payload that the {@link Pigeon} are supposed to carry.
     *
     * @return A {@link DeadPigeonCage}.
     */
    public static <T> DeadPigeonCage<T> instance()
    {
        return (DeadPigeonCage<T>) INSTANCE;
    }


    @NonNull
    @Override
    public Pigeon<T> pigeon(@NonNull T t)
    {
        return new DeadPigeon<>();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // nothing to be done
    }


    public final static Creator<DeadPigeonCage<?>> CREATOR = new Creator<DeadPigeonCage<?>>()
    {
        @Override
        public DeadPigeonCage<?> createFromParcel(Parcel source)
        {
            return INSTANCE;
        }


        @Override
        public DeadPigeonCage<?>[] newArray(int size)
        {
            return new DeadPigeonCage<?>[size];
        }
    };
}
