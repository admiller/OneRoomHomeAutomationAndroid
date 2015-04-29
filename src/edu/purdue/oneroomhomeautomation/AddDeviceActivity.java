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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class AddDeviceActivity extends Activity {

	private final boolean ATTEMPT_TO_CONNECT = true;
	
	EditText deviceNameEditText;
	ToggleButton deviceToggleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adddevice);
		Button submitButton = (Button) findViewById(R.id.buttonSubmitNewDevice);
		submitButton.setOnClickListener(submitListener);
		Button cancelButton = (Button) findViewById(R.id.buttonCancelNewDevice);
		cancelButton.setOnClickListener(cancelListener);
		deviceNameEditText = (EditText) findViewById(R.id.editTextNewDevice);
		deviceToggleButton = (ToggleButton) findViewById(R.id.toggleButtonNewDevice);
		getActionBar().setTitle("Add Device");
	}

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			int resp = -1;
			if (ATTEMPT_TO_CONNECT) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://104.254.216.237/oneroom/phpscripts/createUtil.php");
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("name", deviceNameEditText
							.getText().toString()));
					Log.d("DEBUG", deviceNameEditText.getText().toString());
					if(deviceToggleButton.isChecked()){
						nameValuePairs.add(new BasicNameValuePair("state","1"));
						Log.d("DEBUG", "1");
					}else{
						nameValuePairs.add(new BasicNameValuePair("state","0"));	
						Log.d("DEBUG", "2");
					}
					
					//HttpResponse response = httpclient.execute(httppost);
					//HttpEntity entity = response.getEntity();
					//String responseString = EntityUtils.toString(entity, "UTF-8");
					//resp = Integer.parseInt(responseString);
					
					
					if(resp>=0){
						new AlertDialog.Builder(v.getContext())
						.setTitle("Congratulations!")
						.setMessage("Device Created Successfully!")
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int which) {
										Intent dashboardIntent = new Intent(v.getContext(),DashboardItemListActivity.class);
										startActivity(dashboardIntent);
										finish();
									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
					}
					else{
						new AlertDialog.Builder(v.getContext())
						.setTitle("Register Error")
						.setMessage("A user with that email already exists!")
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
						deviceNameEditText.setText("");
						deviceToggleButton.setChecked(false);
					}
				}catch(Exception e){
					Log.d("DEBUG", "SOMETHING WENT WRONG!", e);
				}
			}
			finish();
		}
	};

	private OnClickListener cancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Close editing page
			finish();
		}
	};

}
