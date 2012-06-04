package ru.evilduck.framework.ui;

import ru.evilduck.framework.SFBaseActivity;
import ru.evilduck.framework.handlers.impl.TestActionHandler;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import ru.evilduck.framework.R;

public class DemoActivity extends SFBaseActivity {

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
	progressDialog.setCancelable(false);

	return progressDialog;
    }

    @Override
    protected void onResume() {
	super.onResume();

	if (requestId != -1 && !getServiceHelper().isPending(requestId)) {
	    dismissDialog(0);
	}
    }

    private void doIt() {
	showDialog(0);
	requestId = getServiceHelper().exampleAction(text1.getText().toString(), text2.getText().toString());
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
	super.onServiceCallback(requestId, requestIntent, resultCode, resultData);

	if (TestActionHandler.ACTION_EXAMPLE_ACTION.equals(requestIntent.getAction())) {
	    if (resultCode == TestActionHandler.RESPONSE_SUCCESS) {
		Toast.makeText(this, resultData.getString("data"), Toast.LENGTH_LONG).show();
	    } else {
		Toast.makeText(this, resultData.getString("error"), Toast.LENGTH_LONG).show();
	    }
	    dismissDialog(0);
	}
    }

}