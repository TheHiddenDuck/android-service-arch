package ru.evilduck.framework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public abstract class SFBaseActivity extends Activity implements SFServiceCallbackListener {

    private SFServiceHelper serviceHelper;

    protected SFApplication getApp() {
	return (SFApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	serviceHelper = getApp().getServiceHelper();
    }

    @Override
    protected void onResume() {
	super.onResume();
	serviceHelper.addListener(this);
    }

    @Override
    protected void onPause() {
	super.onPause();
	serviceHelper.removeListener(this);
    }

    public SFServiceHelper getServiceHelper() {
	return serviceHelper;
    }

    /**
     * Called when a service request finishes executing.
     * 
     * @param requestId
     *            original request id
     * @param original
     *            request data
     * @param resultCode
     *            result of execution code
     * @param resultData
     *            result data
     */
    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
    }

}
