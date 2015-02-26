package edu.purdue.oneroomhomeautomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class LoginScreenActivity extends Activity implements OnClickListener {

	EditText user;
	EditText pass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginscreen);
		Button loginButton = (Button) findViewById(R.id.buttonLogin);
		user = (EditText)findViewById(R.id.editTextUsername);
		pass = (EditText)findViewById(R.id.editTextPassword);
		user.setText("");
		user.setHint("Username");
		pass.setHint("Password");
		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		attemptLogin();
	}
	
	private void attemptLogin() {
	   int success = -1;
	   HttpClient httpclient = new DefaultHttpClient();
	   HttpPost httppost = new HttpPost("http://104.254.216.237/autopi/login.php");
	 
	   try{
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("username", user.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("password", pass.getText().toString()));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        String responseString = EntityUtils.toString(entity, "UTF-8");
	        success=Integer.parseInt(responseString);
	        Log.d("DEBUG", String.valueOf(success));
	    }catch(Exception e){
	    	Log.d("DEBUG", "SOMETHING WENT WRONG!", e);
	    }
	    if(success>=0){
	    	Intent dashboardIntent = new Intent(this, DashboardItemListActivity.class);
	    	startActivity(dashboardIntent);
	    	finish();
	    }else
	    	new AlertDialog.Builder(this)
	        .setTitle("Invalid Login")
	        .setMessage("Username or Password not found!")
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	               
	            }
	         })
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .show();
	        user.setText("");
	        pass.setText("");
	  }
}

	
