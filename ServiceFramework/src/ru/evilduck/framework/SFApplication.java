package ru.evilduck.framework;

import android.app.Application;
import android.content.Context;

public class SFApplication extends Application {

    public static final String PACKAGE = "ru.evilduck.framework";

    private SFServiceHelper serviceHelper;

    @Override
    public void onCreate() {
	super.onCreate();
	serviceHelper = new SFServiceHelper(this);
    }

    public SFServiceHelper getServiceHelper() {
	return serviceHelper;
    }

    public static SFApplication getApplication(Context context) {
	if (context instanceof SFApplication) {
	    return (SFApplication) context;
	}
	return (SFApplication) context.getApplicationContext();
    }

}
