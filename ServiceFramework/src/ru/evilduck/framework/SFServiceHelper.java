package ru.evilduck.framework;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import ru.evilduck.framework.handlers.impl.TestActionHandler;
import ru.evilduck.framework.service.SFCommandExecutorService;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.SparseArray;

public class SFServiceHelper {

    private ArrayList<SFServiceCallbackListener> currentListeners = new ArrayList<SFServiceCallbackListener>();

    private AtomicInteger idCounter = new AtomicInteger();

    private SparseArray<Intent> pendingActivities = new SparseArray<Intent>();

    private Application application;

    SFServiceHelper(Application app) {
	this.application = app;
    }

    public void addListener(SFServiceCallbackListener currentListener) {
	currentListeners.add(currentListener);
    }

    public void removeListener(SFServiceCallbackListener currentListener) {
	currentListeners.remove(currentListener);
    }

    // =========================================

    public int exampleAction(String argumentA, String argumentB) {
	final int requestId = createId();

	Intent i = createIntent(application, TestActionHandler.ACTION_EXAMPLE_ACTION, requestId);
	i.putExtra(TestActionHandler.EXTRA_PARAM_1, argumentA);
	i.putExtra(TestActionHandler.EXTRA_PARAM_2, argumentB);

	return runRequest(requestId, i);
    }

    // =========================================

    public boolean isPending(int requestId) {
	return pendingActivities.get(requestId) != null;
    }

    private int createId() {
	return idCounter.getAndIncrement();
    }

    private int runRequest(final int requestId, Intent i) {
	pendingActivities.append(requestId, i);
	application.startService(i);
	return requestId;
    }

    private Intent createIntent(final Context context, String actionLogin, final int requestId) {
	Intent i = new Intent(context, SFCommandExecutorService.class);
	i.setAction(actionLogin);

	i.putExtra(SFCommandExecutorService.EXTRA_STATUS_RECEIVER, new ResultReceiver(new Handler()) {
	    @Override
	    protected void onReceiveResult(int resultCode, Bundle resultData) {
		Intent originalIntent = pendingActivities.get(requestId);
		if (isPending(requestId)) {
		    pendingActivities.remove(requestId);

		    for (SFServiceCallbackListener currentListener : currentListeners) {
			if (currentListener != null) {
			    currentListener.onServiceCallback(requestId, originalIntent, resultCode, resultData);
			}
		    }
		}
	    }
	});

	return i;
    }

}
