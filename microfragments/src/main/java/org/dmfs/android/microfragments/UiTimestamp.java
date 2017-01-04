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

package org.dmfs.android.microfragments;

import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * A {@link Timestamp} that originated on the main thread.
 *
 * @author Marten Gajda
 */
public final class UiTimestamp implements Timestamp, Parcelable
{
    private final long mTimestamp;


    public UiTimestamp()
    {
        this(System.nanoTime() / 1000000 /* convert to millis */);
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
    public long millis()
    {
        return mTimestamp;
    }


    @Override
    public boolean isAfter(Timestamp other)
    {
        return mTimestamp > other.millis();
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
}
