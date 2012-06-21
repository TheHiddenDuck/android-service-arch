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
package ru.evilduck.framework.service;

import ru.evilduck.framework.SFApplication;
import ru.evilduck.framework.handlers.impl.TestActionHandler;
import android.app.IntentService;
import android.content.Intent;
import android.os.Process;
import android.os.ResultReceiver;
import android.text.TextUtils;

public class SFCommandExecutorService extends IntentService {

	private static final String TAG = "CommandExecutorService";

	public static final String EXTRA_REQUEST_ID = SFApplication.PACKAGE
			.concat(".EXTRA_REQUEST_ID");

	public static final String EXTRA_STATUS_RECEIVER = SFApplication.PACKAGE
			.concat(".STATUS_RECEIVER");

	public SFCommandExecutorService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		String action = intent.getAction();
		if (!TextUtils.isEmpty(action)) {
			ResultReceiver receiver = getReceiver(intent);

			if (TestActionHandler.ACTION_EXAMPLE_ACTION.equals(action)) {
				new TestActionHandler().execute(intent,
						getApplicationContext(), receiver);
			}
		}
	}

	private ResultReceiver getReceiver(Intent intent) {
		return intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
	}

}
