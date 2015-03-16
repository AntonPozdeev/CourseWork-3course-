package com.quickblox.sample.chat.ui.activities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.SmackException;
import org.xbill.DNS.EDNSOption;

import com.google.android.gcm.GCMRegistrar;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.messages.QBMessages;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.sample.chat.ApplicationSingleton;
import com.quickblox.sample.chat.R;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemSelectedListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	public static String UserFullName = null;
    public static String UserEmail = null;
    public static String UserLogin = null;
    public static String UserPassword = null;
    public static String UserPhone = null;
    static int i = 0;
	private QBChatService chatService;
	static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 30;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(RegistrationActivity.this, RegistrationAct.class);
    	startActivityForResult(intent, 3);
    	if(savedInstanceState != null){
    		UserFullName=savedInstanceState.getString("fullname");
    		UserEmail=savedInstanceState.getString("email");
    		UserLogin = savedInstanceState.getString("login");
    		UserPassword = savedInstanceState.getString("password");
    		UserPhone = savedInstanceState.getString("phone");
		}       
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInatanceState){
		savedInatanceState.putString("fullname", UserFullName);
		savedInatanceState.putString("email",UserEmail);
		savedInatanceState.putString("login", UserLogin);
		savedInatanceState.putString("password", UserPassword);
		savedInatanceState.putString("phone", UserPhone);
		super.onSaveInstanceState(savedInatanceState);
	}
	
	@Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (data == null) {return;}
		  UserFullName=data.getStringExtra("fullname");
		  UserEmail=data.getStringExtra("email");
		  UserLogin = data.getStringExtra("login");
		  UserPassword = data.getStringExtra("password");
		  UserPhone = data.getStringExtra("phone");
		  checkUser();
  }
        private void checkUser(){
        	
        QBChatService.setDebugEnabled(true);
        QBSettings.getInstance().fastConfigInit("18203", "CYE6AujUGSedwsB", "D7RUECLeAxP-zhC");
        
        
        if (!QBChatService.isInitialized()) {
            QBChatService.init(this);
        }
        chatService = QBChatService.getInstance();
        final QBUser user = new QBUser();
        user.setFullName(UserFullName);
        user.setEmail(UserEmail);
        user.setLogin(UserLogin);
        user.setPassword(UserPassword);
        user.setPhone(UserPhone);
        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>(){
        	@Override
        	public void onSuccess(QBSession session, Bundle args) {
        		// save current user
		        //
		        user.setId(session.getUserId());
		        ((ApplicationSingleton)getApplication()).setCurrentUser(user);
		        final int code = ((int)(Math.random()*899)-100);
				Log.d("code = ", Integer.toString(code));
				Mail m = new Mail("pozdeev1994@gmail.com", "30111994axel"); 
		        String[] toArr = {user.getEmail().toString()}; 
		        m.setTo(toArr); 
		        m.setFrom("anton-pozdeev@mail.ru"); 
		        m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
		        m.setBody(Integer.toString(code)); 
		        try { 
//		          m.addAttachment("/sdcard/bday.jpg"); 
		          if(m.send()) { 
		            Toast.makeText(getApplicationContext(), "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
		          } else { 
		            Toast.makeText(getApplicationContext(), "Email was not sent.", Toast.LENGTH_LONG).show(); 
		          } 
		        } catch(Exception e) { 
		          Log.e("MailApp", "Could not send email", e); 
		          Toast.makeText(getApplicationContext(), "Could not send email" + e.toString(), Toast.LENGTH_LONG).show(); 
		        } 
		        final int e;
		        
//		        while (i!=2){
		        	final EditText input = new EditText(RegistrationActivity.this);
//		        	input.setInputType()
			        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
		            builder.setTitle("Control")
							.setMessage("Write code:")
							.setView(input)// Добавим поле ввода
		                    .setNeutralButton("Confirm",
		                            new DialogInterface.OnClickListener() {
		                                public void onClick(DialogInterface dialog,
		                                        int id) {
		                                			int vved = 0;
		                                			try {
		                                				vved =Integer.valueOf(input.getText().toString());
		                                			} catch (Exception e2) {
		                                				// TODO: handle exception
		                                			}
		                                			if(code==vved){
		                                				Toast.makeText(getApplicationContext(), "Code is equals", Toast.LENGTH_LONG).show();
		                                				signUpUser(user);
		                                			}
		                                			else {
		                                					Toast.makeText(getApplicationContext(), "Code is not equals", Toast.LENGTH_LONG).show();
		                                					Intent intent = new Intent(RegistrationActivity.this,RegistrationAct.class);
		                                					startActivity(intent);
		                                					finish();
		                                					}
		                                			}
		                            }).create().show();
		            // Sign up to Chat
		        
		            GCMRegistrar.checkDevice(RegistrationActivity.this);
	                GCMRegistrar.checkManifest(RegistrationActivity.this);
	         
	                final String regId = GCMRegistrar.getRegistrationId(RegistrationActivity.this);
	                if (regId.equals("")) {
	                    GCMRegistrar.register(RegistrationActivity.this,  "240590367572");
	                    Toast.makeText(getApplicationContext(), "non registered",Toast.LENGTH_LONG).show();
	                } else {
	                    Toast.makeText(getApplicationContext(), "Already registered",Toast.LENGTH_LONG).show();
//	                    GCMRegistrar.register(RegistrationActivity.this,  "240590367572");
	                    // Subscribe to Push Notifications
	                    subscribeToPushNotifications(regId);
	                }
		        //////////////////////////////////////////////////////////
		        }
        	@Override
		    public void onError(List<String> errors) {
        		AlertDialog.Builder dialog = new AlertDialog.Builder(RegistrationActivity.this);
		        dialog.setMessage("create session errors: " + errors).create().show();
		        Toast.makeText(getApplicationContext(),"create session errors: " + errors.toString() ,
	            		Toast.LENGTH_LONG).show();
		    }});
        }
    private void signUpUser(final QBUser user){
    	QBUsers.signUpSignInTask(user, new QBEntityCallbackImpl<QBUser>() {
	        public void onSuccess(QBUser result, Bundle params) {
	            // success
	        	result.setPassword(UserPassword);
	        	loginToChat(user);
		        		        	
	        }
	        public void onError(List<String> errors) {
	            AlertDialog.Builder dialog = new AlertDialog.Builder(RegistrationActivity.this);
	            dialog.setMessage("register errors: " + errors).create().show();
	            Toast.makeText(getApplicationContext(),"register errors: " + errors.toString() ,
	            		Toast.LENGTH_LONG).show();
	            Intent intent = new Intent(RegistrationActivity.this, RegistrationAct.class);
	            startActivity(intent);
	            finish();
	        }
	    });}

    private void loginToChat(final QBUser user){
        chatService.login(user, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {

                // Start sending presences
                //
                try {
                    chatService.startAutoSendPresence(AUTO_PRESENCE_INTERVAL_IN_SECONDS);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }
                writeFile(user.getLogin(),"login");
                writeFile(user.getPassword(),"pswd");
                // go to Dialogs screen
                //
                SplashActivity.User_Id = user.getId();
                Intent intent = new Intent(RegistrationActivity.this, DialogsActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(List errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(RegistrationActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();
                Toast.makeText(getApplicationContext(),"chat login errors: " + errors.toString() ,
	            		Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void subscribeToPushNotifications(String regIdq) {
		// TODO Auto-generated method stub
    	String deviceId = ((TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        
        QBMessages.subscribeToPushNotificationsTask(regIdq, deviceId, QBEnvironment.DEVELOPMENT, null, new QBEntityCallbackImpl<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> subscriptions, Bundle args) {
                Log.d("subscribe", "subscribed");
                Toast.makeText(getApplicationContext(), "[subscribe registered",Toast.LENGTH_LONG).show();
            }
     
            @Override
            public void onError(List<String> errors) {
     
            }
        });
	}
    
    public void writeFile(String login, String nameFile) {
        try {
          // отрываем поток для записи
          BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
              openFileOutput(nameFile, MODE_PRIVATE)));
          // пишем данные
          bw.write(login);
          // закрываем поток
          bw.close();
          Log.d("file", "Файл записан");
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
        
    public void onBackPressed(){
    	
    }
}
    
