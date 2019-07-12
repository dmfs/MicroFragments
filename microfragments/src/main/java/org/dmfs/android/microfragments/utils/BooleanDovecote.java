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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.Pigeon;
import org.dmfs.pigeonpost.localbroadcast.tools.MainThreadExecutor;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


/**
 * A {@link Dovecote} for {@link Pigeon}s that carry {@link Boolean} values.
 * <p>
 * TODO: move to PigeonPost project
 *
 * @author Marten Gajda
 */
public final class BooleanDovecote implements Dovecote<Boolean>
{
    private final Context mContext;
    private final String mName;
    private final BroadcastReceiver mReceiver;


    public BooleanDovecote(@NonNull Context context, @NonNull String name, @NonNull OnPigeonReturnCallback<Boolean> callback)
    {
        this.mContext = context;
        this.mName = name;
        this.mReceiver = new BooleanDovecote.DovecotReceiver(callback);
        LocalBroadcastManager.getInstance(context).registerReceiver(this.mReceiver, new IntentFilter(this.mName));
    }


    @NonNull
    @Override
    public Cage<Boolean> cage()
    {
        return new BooleanCage(new Intent(this.mName));
    }


    public void dispose()
    {
        LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mReceiver);
    }


    private static class DovecotReceiver extends BroadcastReceiver
    {
        private final OnPigeonReturnCallback<Boolean> mCallback;


        private DovecotReceiver(@NonNull OnPigeonReturnCallback<Boolean> callback)
        {
            this.mCallback = callback;
        }


        public void onReceive(Context context, final Intent intent)
        {
            MainThreadExecutor.INSTANCE.execute(new Runnable()
            {
                public void run()
                {
                    BooleanDovecote.DovecotReceiver.this.mCallback.onPigeonReturn(intent.getBooleanExtra("org.dmfs.pigeonpost.DATA", false));
                }
            });
        }
    }
}
