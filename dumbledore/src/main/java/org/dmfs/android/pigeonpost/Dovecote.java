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

package org.dmfs.android.pigeonpost;

import android.support.annotation.NonNull;


/**
 * The home where the {@link Pigeon}s return to with their payload.
 *
 * @param <T>
 *         The type of payload {@link Pigeon}s can carry to this {@link Dovecote}.
 *
 * @author Marten Gajda
 */
public interface Dovecote<T>
{
    /**
     * A callback interface that notifies the owner of returning {@link Pigeon}s.
     *
     * @param <T>
     *         The type of the payload carried by the {@link Pigeon}s of the {@link Dovecote}.
     */
    interface OnPigeonReturnCallback<T>
    {
        /**
         * Called when a {@link Pigeon} returned with a payload.
         *
         * @param payload
         *         The payload carried by the {@link Pigeon}.
         */
        void onPigeonReturn(@NonNull T payload);
    }

    /**
     * Returns a {@link Cage} with {@link Pigeon}s that will return to this {@link Dovecote}.
     *
     * @return A {@link Cage}.
     */
    @NonNull
    Cage<T> cage();

    /**
     * Dispose this {@link Dovecote},releasing any bound resources.
     */
    void dispose();
}
