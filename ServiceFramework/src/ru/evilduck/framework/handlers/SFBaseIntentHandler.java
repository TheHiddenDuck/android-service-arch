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
