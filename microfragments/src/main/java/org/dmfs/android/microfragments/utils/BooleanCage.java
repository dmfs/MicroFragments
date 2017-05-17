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

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Pigeon;


/**
 * A {@link Cage} that contains {@link Pigeon}s that carry {@link Boolean} values.
 * <p>
 * TODO: move to PigeonPost project
 *
 * @author Marten Gajda
 */
public final class BooleanCage implements Cage<Boolean>
{
    private final Intent mIntent;
    public static final Creator<BooleanCage> CREATOR = new Creator<BooleanCage>()
    {
        public BooleanCage createFromParcel(Parcel source)
        {
            return new BooleanCage((Intent) source.readParcelable(this.getClass().getClassLoader()));
        }


        public BooleanCage[] newArray(int size)
        {
            return new BooleanCage[size];
        }
    };


    public BooleanCage(@NonNull Intent intent)
    {
        this.mIntent = intent;
    }


    @NonNull
    public Pigeon<Boolean> pigeon(@NonNull final Boolean payload)
    {
        final Intent originalIntent = this.mIntent;
        return new Pigeon<Boolean>()
        {
            public void send(@NonNull Context context)
            {
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                Intent intent = new Intent(originalIntent);
                if (payload)
                {
                    intent.putExtra("org.dmfs.pigeonpost.DATA", true);
                }
                localBroadcastManager.sendBroadcastSync(intent);
            }
        };
    }


    public int describeContents()
    {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(this.mIntent, flags);
    }
}
