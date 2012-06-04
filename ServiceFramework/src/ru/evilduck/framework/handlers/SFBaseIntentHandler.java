/*
 * Copyright (C) 2012 Alexander Osmanov (http://www.perfectearapp.com)
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public abstract class SFBaseIntentHandler {

    public static final int RESPONSE_SUCCESS = 0;

    public static final int RESPONSE_FAILURE = 1;

    public final void execute(Intent intent, Context context, ResultReceiver callback) {
	this.sfCallback = callback;
	doExecute(intent, context, callback);
    }

    public abstract void doExecute(Intent intent, Context context, ResultReceiver callback);

    private ResultReceiver sfCallback;

    private int result;

    public int getResult() {
	return result;
    }

    protected void sendUpdate(int resultCode, Bundle data) {
	result = resultCode;
	if (sfCallback != null) {
	    sfCallback.send(resultCode, data);
	}
    }

}
