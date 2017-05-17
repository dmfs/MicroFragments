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

import android.os.Parcelable;
import android.support.annotation.NonNull;


/**
 * A timestamp in nanoSeconds. This is only mean for comparisons with other {@link Timestamp}s. In particular don't compare the result with and absolute time or
 * local time and don't try to convert it to such.
 *
 * @author Marten Gajda
 */
public interface Timestamp extends Parcelable
{
    /**
     * Return the time of this timestamp in nanoSeconds. Note the result is not an absolute time as returned by {@link System#currentTimeMillis()}.
     *
     * @return Tha nanoSeconds since the Epoch of this timestamp.
     */
    long nanoSeconds();

    /**
     * Checks whether this {@link Timestamp} is after the given one.
     *
     * @param other
     *         Another {@link Timestamp}.
     *
     * @return {@code true} if this {@link Timestamp}is after the the given once, {@code false} otherwise.
     */
    boolean isAfter(@NonNull Timestamp other);
}
