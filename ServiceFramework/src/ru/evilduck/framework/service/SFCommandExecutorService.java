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

    public static final String EXTRA_REQUEST_ID = SFApplication.PACKAGE + ".EXTRA_REQUEST_ID";

    public static final String EXTRA_STATUS_RECEIVER = SFApplication.PACKAGE + ".STATUS_RECEIVER";

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
		new TestActionHandler().execute(intent, getApplicationContext(), receiver);
	    }
	}
    }

    private ResultReceiver getReceiver(Intent intent) {
	return intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
    }

}
