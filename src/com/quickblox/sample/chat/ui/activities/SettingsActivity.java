package com.quickblox.sample.chat.ui.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.sample.chat.R;
import com.quickblox.sample.chat.ui.adapters.DialogsAdapter;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SettingsActivity extends Activity {
	
	EditText 	fullname;
	EditText	email;
	EditText 	oldPassword;
	EditText 	newPassword;
	EditText 	confirmNewPassword;
	EditText 	phone;
	Button 		save;
	Button 		cansel;
	boolean		saveSetting=false;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.settings_activity);
	        init();
	        final QBUser user = new QBUser(SplashActivity.USER_LOGIN);
	        user.setId(SplashActivity.User_Id);
	        save.setOnClickListener(new View.OnClickListener() {
				boolean flag = true;
				@Override
				public void onClick(View arg0) {
					if(fullname.getText().toString().isEmpty() &&
							email.getText().toString().isEmpty() &&
							oldPassword.getText().toString().isEmpty() &&
							newPassword.getText().toString().isEmpty() &&
							confirmNewPassword.getText().toString().isEmpty() &&
							phone.getText().toString().isEmpty()){
						Toast.makeText(getApplicationContext(),
				        		"Settings are not changed",
				                Toast.LENGTH_LONG).show();
			        	Intent intent = new Intent(SettingsActivity.this,DialogsActivity.class);
						startActivity(intent);
						finish();
					}
					else{
						 new AlertDialog.Builder(SettingsActivity.this)
					        .setTitle("Change pesonal information")
					        .setMessage("Do you want save?")
					        .setNegativeButton(android.R.string.no, null)
					        .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface arg0, int arg1) {
					            	if(!fullname.getText().toString().isEmpty()){
										user.setFullName(fullname.getText().toString());
									}
									
									if(!email.getText().toString().isEmpty()){
										user.setEmail(email.getText().toString());
									}
									
									if(!oldPassword.getText().toString().isEmpty()){
										if(oldPassword.getText().toString().equals(SplashActivity.USER_PASSWORD)){
											if(oldPassword.getText().toString().equals(newPassword.getText().toString())){
												Toast.makeText(getApplicationContext(),
										        		"The OLD PASSWORD must not coincide with a NEW PASSWORD",
										                Toast.LENGTH_LONG).show();
												flag = false;
												return;
											}
											
											if(!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){
												Toast.makeText(getApplicationContext(),
										        		"The NEW PASSWORD and CONFIRM are not equal",
										                Toast.LENGTH_LONG).show();
												flag = false;
												return;
											}
											
											if(newPassword.length()<8){
												Toast.makeText(getApplicationContext(),
										        		"PASSWORD not be less than 8 characters",
										                Toast.LENGTH_LONG).show();
												flag = false;
												return;
											}
											else{
												user.setOldPassword(SplashActivity.USER_PASSWORD);
												user.setPassword(newPassword.getText().toString());
												SplashActivity.USER_PASSWORD = newPassword.getText().toString();
											}
										}
										else{
											Toast.makeText(getApplicationContext(),
									        		"The old password is not correct",
									                Toast.LENGTH_LONG).show();
											flag = false;
										}
									}
									
									if(!phone.getText().toString().isEmpty()){
										user.setPhone(phone.getText().toString());
									}
									
									if(flag){
										
										QBUsers.updateUser(user,new QBEntityCallbackImpl<QBUser>() {
									        public void onSuccess(QBUser result, Bundle params) {
									            // success
									        	Toast.makeText(getApplicationContext(),
										        		"Success",
										                Toast.LENGTH_LONG).show();
									        	Intent intent = new Intent(SettingsActivity.this,DialogsActivity.class);
												startActivity(intent);
												finish();
									        }
					
									        public void onError(List<String> errors) {
									        	Toast.makeText(getApplicationContext(),
										        		errors.toString(),
										                Toast.LENGTH_LONG).show();
									        }
									    });	
									}
					            }
					            }).create().show();
					}
				}
			});
	        cansel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SettingsActivity.this,DialogsActivity.class);
					startActivity(intent);
					finish();
				}
	        	
	        });
	 }
	 
	 private void init(){
		 fullname = (EditText)findViewById(R.id.editFullName);
		 email = (EditText)findViewById(R.id.editEmail);
		 oldPassword = (EditText)findViewById(R.id.editOldPassword);
		 newPassword = (EditText)findViewById(R.id.editNewPassword);
		 confirmNewPassword = (EditText)findViewById(R.id.editConfirm);
		 phone = (EditText)findViewById(R.id.editPhone);
		 save = (Button)findViewById(R.id.buttonSave);
		 cansel = (Button)findViewById(R.id.buttonCansel);
	 }
	 
	    @Override
	    public void onBackPressed() {
	       
	    }

	}
