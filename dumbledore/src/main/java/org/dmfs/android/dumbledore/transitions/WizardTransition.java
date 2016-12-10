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

package org.dmfs.android.dumbledore.transitions;

import android.content.Context;
import android.support.annotation.NonNull;

import org.dmfs.android.dumbledore.WizardStep;


/**
 * A transition to another {@link WizardStep}.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public interface WizardTransition
{

    /**
     * Executes this transition.
     *
     * @param context
     *         A {@link Context}.
     */
    void execute(@NonNull Context context);
}
