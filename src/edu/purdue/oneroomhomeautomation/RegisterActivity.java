package edu.purdue.oneroomhomeautomation;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {

	/**
	 * Set this to true if you want to try to legitimately register. If you are
	 * doing tests without connecting to the DB, set this to false
	 */
	private final boolean ATTEMPT_TO_CONNECT = false;

	EditText name;
	EditText user;
	EditText pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		name = (EditText) findViewById(R.id.editTextRegisterName);
		user = (EditText) findViewById(R.id.editTextRegisterUsername);
		pass = (EditText) findViewById(R.id.editTextRegisterPassword);
		user.setText("");
		user.setHint("Username");
		pass.setHint("Password");
		Button submitButton = (Button) findViewById(R.id.buttonRegisterSubmit);
		submitButton.setOnClickListener(registerSubmitOnClickListener);
	}

	private OnClickListener registerSubmitOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Insert register code here
			if (ATTEMPT_TO_CONNECT) {
				// register
			} else {
				finish();
			}
		}
	};

}
