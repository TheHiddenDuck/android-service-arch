package ru.evilduck.framework.handlers.impl;

import ru.evilduck.framework.SFApplication;
import ru.evilduck.framework.handlers.SFBaseIntentHandler;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

public class TestActionHandler extends SFBaseIntentHandler {

    private static final String TAG = "TestActionHandler";

    public static String ACTION_EXAMPLE_ACTION = SFApplication.PACKAGE.concat(".ACTION_EXAMPLE_ACTION");

    public static String EXTRA_PARAM_1 = SFApplication.PACKAGE.concat(".EXTRA_PARAM_1");

    public static String EXTRA_PARAM_2 = SFApplication.PACKAGE.concat(".EXTRA_PARAM_2");

    @Override
    public void doExecute(Intent intent, Context context, ResultReceiver callback) {
	String arg1 = intent.getStringExtra(EXTRA_PARAM_1);
	String arg2 = intent.getStringExtra(EXTRA_PARAM_2);
	Bundle data = new Bundle();

	try {
	    Thread.sleep(2000);
	} catch (InterruptedException e) {
	    Log.wtf(TAG, "WTF");
	}

	if (TextUtils.isEmpty(arg1) || TextUtils.isEmpty(arg2)) {
	    data.putString("error", "Surprise mothafucka!");
	    sendUpdate(RESPONSE_FAILURE, data);
	} else {
	    data.putString("data", arg1 + arg2);
	    sendUpdate(RESPONSE_SUCCESS, data);
	}
    }

}
