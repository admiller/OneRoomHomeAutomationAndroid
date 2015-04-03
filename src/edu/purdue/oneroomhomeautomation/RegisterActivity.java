package edu.purdue.oneroomhomeautomation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
//test
	/**
	 * Set this to true if you want to try to legitimately register. If you are
	 * doing tests without connecting to the DB, set this to false
	 */
	private final boolean ATTEMPT_TO_CONNECT = true;
	
	EditText name;
	EditText email;
	EditText pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		name = (EditText) findViewById(R.id.editTextRegisterName);
		email = (EditText) findViewById(R.id.editTextRegisterUsername);
		pass = (EditText) findViewById(R.id.editTextRegisterPassword);
		name.setText("");
		name.setHint("Name");
		email.setText("");
		email.setHint("Email");
		pass.setHint("Password");
		Button submitButton = (Button) findViewById(R.id.buttonRegisterSubmit);
		submitButton.setOnClickListener(registerSubmitOnClickListener);
	}

	private OnClickListener registerSubmitOnClickListener = new OnClickListener() {
		@Override
		public void onClick(final View arg0) {
			// TODO Insert register code here
			if (ATTEMPT_TO_CONNECT) {
				int id = -1;
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://104.254.216.237/oneroom/phpscripts/register.php");

				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("name", name
							.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("email", email
							.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("password", pass
							.getText().toString()));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					String responseString = EntityUtils.toString(entity, "UTF-8");
					//JSONArray r = new JSONArray();
					//id = r.getInt(0);
					id = Integer.parseInt(responseString);
					
					Log.d("DEBUG", responseString);
				} catch (Exception e) {
					Log.d("DEBUG", "SOMETHING WENT WRONG!", e);
				}
				if (id >= 0) {
					new AlertDialog.Builder(arg0.getContext())
					.setTitle("Congratulations!")
					.setMessage("User created successfully!")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int which) {
									Intent dashboardIntent = new Intent(arg0.getContext(),DashboardItemListActivity.class);
									startActivity(dashboardIntent);
									finish();
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
			
				} else
					new AlertDialog.Builder(arg0.getContext())
							.setTitle("Register Error")
							.setMessage("A user with that email already exists!")
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {

										}
									}).setIcon(android.R.drawable.ic_dialog_alert)
							.show();
				name.setText("");
				email.setText("");
				pass.setText("");
			} else {
				finish();
			}
		}
	};

}
