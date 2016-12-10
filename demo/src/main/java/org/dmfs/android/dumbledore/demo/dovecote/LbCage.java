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

package org.dmfs.android.dumbledore.demo.dovecote;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import org.dmfs.android.pigeonpost.Cage;
import org.dmfs.android.pigeonpost.Pigeon;


/**
 * An {@link Cage} to send {@link Parcelable} objects via a {@link LocalBroadcastManager}.
 *
 * @author Marten Gajda
 */
public final class LbCage<T extends Parcelable> implements Cage<T>
{
    private final Intent mIntent;


    public LbCage(@NonNull Intent intent)
    {
        mIntent = intent;
    }


    @NonNull
    @Override
    public Pigeon<T> pigeon(@NonNull final T payload)
    {
        return new Pigeon<T>()
        {
            @Override
            public void send(Context context)
            {
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                Intent intent = new Intent(mIntent);
                intent.putExtra("data", payload);
                localBroadcastManager.sendBroadcast(intent);
            }
        };
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mIntent, flags);
    }


    public final static Creator<LbCage> CREATOR = new Creator<LbCage>()
    {
        @Override
        public LbCage createFromParcel(Parcel source)
        {
            return new LbCage((Intent) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public LbCage[] newArray(int size)
        {
            return new LbCage[size];
        }
    };
}
