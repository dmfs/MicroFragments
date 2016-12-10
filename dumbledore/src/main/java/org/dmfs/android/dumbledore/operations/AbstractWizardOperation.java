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

package org.dmfs.android.dumbledore.operations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.Locale;


/**
 * An abstract WizardTransition that sends itself to a WizardActivity using a local broadcast.
 * <p>
 * TODO: find a better way to provide this RTL info
 *
 * @author Marten Gajda
 */
public abstract class AbstractWizardOperation implements WizardOperation, Parcelable
{
    @SuppressLint("InlinedApi")
    protected final boolean useRtl(@NonNull Context context)
    {
        if (Build.VERSION.SDK_INT < 17)
        {
            String lang = Locale.getDefault().getLanguage();
            return lang != null && "iw".equals(lang) || "ar".equals(lang);
        }
        Configuration config = context.getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
}
