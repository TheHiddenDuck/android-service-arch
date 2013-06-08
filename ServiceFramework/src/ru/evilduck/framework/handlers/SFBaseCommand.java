/*
 * Copyright (C) 2013 Alexander Osmanov (http://perfectear.educkapps.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ru.evilduck.framework.handlers;

import ru.evilduck.framework.SFApplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;

@SuppressLint("ParcelCreator")
public abstract class SFBaseCommand implements Parcelable {

    public static String EXTRA_PROGRESS = SFApplication.PACKAGE.concat(".EXTRA_PROGRESS");

    public static final int RESPONSE_SUCCESS = 0;

    public static final int RESPONSE_FAILURE = 1;

    public static final int RESPONSE_PROGRESS = 2;

    private ResultReceiver sfCallback;
    
    protected volatile boolean cancelled = false;

    public final void execute(Intent intent, Context context, ResultReceiver callback) {
	this.sfCallback = callback;
	doExecute(intent, context, callback);
    }

    protected abstract void doExecute(Intent intent, Context context, ResultReceiver callback);

    protected void notifySuccess(Bundle data) {
	sendUpdate(RESPONSE_SUCCESS, data);
    }

    protected void notifyFailure(Bundle data) {
	sendUpdate(RESPONSE_FAILURE, data);
    }

    protected void sendProgress(int progress) {
	Bundle b = new Bundle();
	b.putInt(EXTRA_PROGRESS, progress);

	sendUpdate(RESPONSE_PROGRESS, b);
    }

    private void sendUpdate(int resultCode, Bundle data) {
	if (sfCallback != null) {
	    sfCallback.send(resultCode, data);
	}
    }

    public synchronized void cancel() {
	cancelled = true;
    }

}
