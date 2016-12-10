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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import org.dmfs.android.pigeonpost.Cage;
import org.dmfs.android.pigeonpost.Dovecote;
import org.dmfs.android.pigeonpost.Pigeon;


/**
 * A {@link Dovecote} that receives {@link Pigeon}s with a {@link LocalBroadcastManager}.
 *
 * @author Marten Gajda
 */
public final class LbDovecote<T extends Parcelable> implements Dovecote<T>
{
    private final Context mContext;
    private final String mName;
    private final OnPigeonReturnCallback<T> mCallback;
    private final BroadcastReceiver mReceiver;


    public LbDovecote(@NonNull Context context, @NonNull String name, @NonNull OnPigeonReturnCallback<T> callback)
    {
        mContext = context;
        mName = name;
        mCallback = callback;
        mReceiver = new MailboxBroadcastReceiver<T>(mCallback);
        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, new IntentFilter(mName));
    }


    @NonNull
    @Override
    public Cage<T> cage()
    {
        return new LbCage<T>(new Intent(mName));
    }


    @Override
    public void dispose()
    {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }


    private static class MailboxBroadcastReceiver<T extends Parcelable> extends BroadcastReceiver
    {
        private final OnPigeonReturnCallback<T> mCallback;


        private MailboxBroadcastReceiver(@NonNull OnPigeonReturnCallback<T> callback)
        {
            mCallback = callback;
        }


        @Override
        public void onReceive(Context context, Intent intent)
        {
            mCallback.onPigeonReturn((T) intent.getParcelableExtra("data"));
        }
    }
}
