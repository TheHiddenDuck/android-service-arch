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
package ru.evilduck.framework.ui;

import ru.evilduck.framework.R;
import ru.evilduck.framework.SFBaseActivity;
import ru.evilduck.framework.handlers.SFBaseCommand;
import ru.evilduck.framework.handlers.impl.TestActionCommand;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class DemoActivity extends SFBaseActivity {

    private static final String PROGRESS_DIALOG = "progress-dialog";

    private EditText text1;

    private EditText text2;

    private int requestId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);

	text1 = (EditText) findViewById(R.id.editText1);
	text2 = (EditText) findViewById(R.id.editText2);
	text2.setOnEditorActionListener(new OnEditorActionListener() {
	    @Override
	    public boolean onEditorAction(TextView textView, int id, KeyEvent event) {
		if (id == EditorInfo.IME_ACTION_DONE) {
		    doIt();
		}
		return false;
	    }
	});

	findViewById(R.id.button_button).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View view) {
		doIt();
	    }

	});
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	ProgressDialog progressDialog = new ProgressDialog(this);
	progressDialog.setMessage("Processing");
	progressDialog.setOnCancelListener(new OnCancelListener() {
	    @Override
	    public void onCancel(DialogInterface dialog) {
		getServiceHelper().cancelCommand(requestId);
	    }
	});

	return progressDialog;
    }

    @Override
    protected void onResume() {
	super.onResume();

	if (requestId != -1 && !getServiceHelper().isPending(requestId)) {
	    dismissProgressDialog();
	}
    }

    private void doIt() {
	ProgressDialogFragment progress = new ProgressDialogFragment();
	progress.show(getSupportFragmentManager(), PROGRESS_DIALOG);

	requestId = getServiceHelper().exampleAction(text1.getText().toString(), text2.getText().toString());
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
	super.onServiceCallback(requestId, requestIntent, resultCode, resultData);

	if (getServiceHelper().check(requestIntent, TestActionCommand.class)) {
	    if (resultCode == TestActionCommand.RESPONSE_SUCCESS) {
		Toast.makeText(this, resultData.getString("data"), Toast.LENGTH_LONG).show();
		dismissProgressDialog();
	    } else if (resultCode == TestActionCommand.RESPONSE_PROGRESS) {
		upodateProgressDialog(resultData.getInt(SFBaseCommand.EXTRA_PROGRESS, -1));
	    } else {
		Toast.makeText(this, resultData.getString("error"), Toast.LENGTH_LONG).show();
		dismissProgressDialog();
	    }
	}
    }

    public void cancelCommand() {
	getServiceHelper().cancelCommand(requestId);
    }

    public static class ProgressDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    ProgressDialog progressDialog = new ProgressDialog(getActivity());
	    progressDialog.setMessage("Result: 0%");

	    return progressDialog;
	}

	public void setProgress(int progress) {
	    ((ProgressDialog) getDialog()).setMessage("Result: " + progress + "%");
	}

	@Override
	public void onCancel(DialogInterface dialog) {
	    super.onCancel(dialog);
	    ((DemoActivity) getActivity()).cancelCommand();
	}

    }

    private void dismissProgressDialog() {
	ProgressDialogFragment progress = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag(
		PROGRESS_DIALOG);
	if (progress != null) {
	    progress.dismiss();
	}
    }

    private void upodateProgressDialog(int progress) {
	ProgressDialogFragment progressDialog = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag(
		PROGRESS_DIALOG);
	if (progressDialog != null) {
	    progressDialog.setProgress(progress);
	}
    }

}