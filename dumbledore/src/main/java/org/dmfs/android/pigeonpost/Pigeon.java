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

import android.content.Context;
import android.support.annotation.NonNull;


/**
 * A pigeon to carry an object of type &lt;T&gt; to its originating {@link Dovecote}.
 *
 * @param <T>
 *         The type of the payload this {@link Pigeon} can transport.
 *
 * @author Marten Gajda
 */
public interface Pigeon<T>
{
    /**
     * Send this pigeon to its {@link Dovecote}.
     *
     * @param context
     *         A {@link Context}.
     */
    void send(@NonNull Context context);
}
