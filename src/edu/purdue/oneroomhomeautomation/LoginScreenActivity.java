package edu.purdue.oneroomhomeautomation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginScreenActivity extends Activity implements OnClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginscreen);
		Button loginButton = (Button) findViewById(R.id.buttonLogin);
		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		attemptLogin();
	}
	
	private void attemptLogin() {
		// TODO Authenticate login
		// Start the DashboardItemListActivity		
		Intent dashboardIntent = new Intent(this, DashboardItemListActivity.class);
		startActivity(dashboardIntent);
		finish();
	}


}
