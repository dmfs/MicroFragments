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

package org.dmfs.android.microfragments.timestamps;

import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;

import org.dmfs.android.microfragments.Timestamp;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;


/**
 * A {@link Timestamp} that originated on the main thread.
 * <p>
 * Note a {@link UiTimestamp} can not be created on a background thread.
 *
 * @author Marten Gajda
 */
public final class UiTimestamp implements Timestamp, Parcelable
{
    private final long mTimestamp;


    /**
     * Creates a {@link UiTimestamp}.
     */
    @MainThread
    public UiTimestamp()
    {
        this(System.nanoTime());
        if (Looper.getMainLooper() != Looper.myLooper())
        {
            throw new IllegalStateException("UiTimestamp must be created on the main thread.");
        }
    }


    private UiTimestamp(long timestamp)
    {
        mTimestamp = timestamp;
    }


    @Override
    public long nanoSeconds()
    {
        return mTimestamp;
    }


    @Override
    public boolean isAfter(@NonNull Timestamp other)
    {
        return 0 > other.nanoSeconds() - mTimestamp;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(mTimestamp);
    }


    public final static Creator<UiTimestamp> CREATOR = new Creator<UiTimestamp>()
    {
        @Override
        public UiTimestamp createFromParcel(Parcel source)
        {
            return new UiTimestamp(source.readLong());
        }


        @Override
        public UiTimestamp[] newArray(int size)
        {
            return new UiTimestamp[size];
        }
    };


    @Override
    public int hashCode()
    {
        return (int) mTimestamp;
    }


    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Timestamp))
        {
            return false;
        }
        return mTimestamp == ((Timestamp) o).nanoSeconds();
    }
}
