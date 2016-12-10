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

import android.os.Parcelable;
import android.support.annotation.NonNull;


/**
 * A cage to return {@link Pigeon}s that can carry an object of type &lt;T&gt;.
 *
 * @param <T>
 *         The type of the pigeon payload.
 *
 * @author Marten Gajda
 */
public interface Cage<T> extends Parcelable
{
    /**
     * Returns a {@link Pigeon} to carry the given object back home.
     *
     * @param payload
     *         The object to carry.
     *
     * @return A {@link Pigeon} that carries the given payload.
     */
    @NonNull
    Pigeon<T> pigeon(@NonNull T payload);
}
