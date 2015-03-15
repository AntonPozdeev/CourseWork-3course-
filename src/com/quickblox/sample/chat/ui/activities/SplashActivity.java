package com.quickblox.sample.chat.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.HeterogeneousExpandableList;
import android.widget.Toast;
//import com.quickblox.;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.server.BaseService;
import com.quickblox.auth.Consts;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.messages.QBMessages;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.sample.chat.ApplicationSingleton;
import com.quickblox.sample.chat.R;
import com.quickblox.sample.message.GCMIntentService;
import com.quickblox.sample.message.GcmBroadcastReceiver;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.google.android.gcm.GCMRegistrar;


public class SplashActivity extends Activity {

    public static final String APP_ID = "18203";
    public static final String AUTH_KEY = "CYE6AujUGSedwsB";
    public static final String AUTH_SECRET = "D7RUECLeAxP-zhC";
    String token;
    public static String USER_LOGIN = null;
    public static String USER_PASSWORD = null;
    static Integer User_Id=null;
    boolean lo=true;
    static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 30;

    private QBChatService chatService;
    String li;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////
        	Intent intent = new Intent(SplashActivity.this, LoginAndPswdActivity.class);
        	startActivityForResult(intent, 1);
        	if(savedInstanceState != null){
        		USER_LOGIN = savedInstanceState.getString("login");
        		USER_PASSWORD = savedInstanceState.getString("password");
			}
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

	@Override
	public void onSaveInstanceState(Bundle savedInatanceState){
		savedInatanceState.putString("login", USER_LOGIN);
		savedInatanceState.putString("password", USER_PASSWORD);
		
		super.onSaveInstanceState(savedInatanceState);
	}
    
    @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (data == null) {return;}
		 USER_LOGIN = data.getStringExtra("login");
		 USER_PASSWORD = data.getStringExtra("password");
		 checkUser();
    }
    public void checkUser(){
    	// Init Chat
        //
    	
			
        QBChatService.setDebugEnabled(true);
        final QBUser user = new QBUser();
        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
        if (!QBChatService.isInitialized()) {
            QBChatService.init(this);
        }
        chatService = QBChatService.getInstance();
        // create QB user
        //
        
        user.setLogin(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        ////////////////////////////
//        Facebook a = new Facebook("1529086197380528");
//        String  facebookAccessToken =a.getAccessToken();
//        facebookAccessToken = a.getAccessToken();
//        QBAuth.createSessionUsingSocialProvider(QBProvider.FACEBOOK, facebookAccessToken, null, new QBEntityCallbackImpl<QBSession>() {
//
//            @Override
//            public void onSuccess(QBSession session,  Bundle args) {
//                Log.i("FacebookCreate", "session created, token = "+session.getToken());
//                Toast.makeText(getApplicationContext(),"Succceessss " ,Toast.LENGTH_LONG).show();
//                loginToChat(user);
//            }
//
//            @Override
//            public void onError(List<String> eroors) {
//            	Log.i("FacebookCreate", "session created, token = "+eroors.toString());
//            	Toast.makeText(getApplicationContext(),eroors.toString() ,Toast.LENGTH_LONG).show();
//            }
//        });}
        ////////////////////////////
        
        QBAuth.createSession(user,new QBEntityCallbackImpl<QBSession>(){
        	
            @Override
            public void onSuccess(QBSession session, Bundle args) {
//            	AppSession.startSession(LoginType.EMAIL, user, token);
                // save current user
            	
            	/////////////////
                
//            	try {
//            	   token = BaseService.getBaseService().getToken();
//            	    Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
//            	    if(token == null){
//            	        // recreate session here
//            	    	Log.d("token", token);
//            	    	Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
//            	    }
//            	} catch (BaseServiceException e) {
//            	    e.printStackTrace();
//            	    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
//            	}
                user.setId(session.getUserId());
                ((ApplicationSingleton)getApplication()).setCurrentUser(user);
//////////////////////////////////////////
//                GCMRegistrar.unregister(getApplicationContext());
                GCMRegistrar.checkDevice(SplashActivity.this);
                GCMRegistrar.checkManifest(SplashActivity.this);

                final String regId = GCMRegistrar.getRegistrationId(SplashActivity.this);
                if (regId.equals("")) {
                    GCMRegistrar.register(SplashActivity.this,  "240590367572");
//                  GCMIntentService.onRegistered(SplashActivity.this,"240590367572");
                    Toast.makeText(getApplicationContext(), "non registered",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Already registered",Toast.LENGTH_LONG).show();
                    // Subscribe to Push Notifications
                    subscribeToPushNotifications(regId);
//                    GCMRegistrar.register(SplashActivity.this,  "240590367572");
                }
//                /////////////////
//                // login to Chat
                //
                loginToChat(user);
            }
//////////////////////////////////
//            public void subscribeToPushNotifications(String registrationID) {
//                
//            }
/////////////////////////////////            
            
            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                dialog.setMessage("create session errors: " + errors).create().show();
                Toast.makeText(getApplicationContext(),"create session errors: " + errors.toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SplashActivity.this, LoginAndPswdActivity.class);
                startActivityForResult(intent, 1);
//                try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
            }
        });
    }
////    public QBUser login(String socialProvider, String accessToken,
////    		  String accessTokenSecret) throws QBResponseException, BaseServiceException {
////    		  QBUser user;
////    		 
////    		  QBSession session = QBAuth.createSession();
////    		 
////    		  user = QBUsers.signInUsingSocialProvider(socialProvider, accessToken, accessTokenSecret);
////    		  user.setPassword(session.getToken());
////    		 
////    		  String token = QBAuth.getBaseService().getToken();
////    		 
////    		  QBAuth.createSessionUsingSocialProvider(QBProvider.FACEBOOK, user, token);
////    		 
////    		  return user;
////    		}
//    
    
    
    
    
    private void loginToChat(final QBUser user){
    	
        chatService.login(user, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {
//            	
                // Start sending presences
                //
            	try {
            	    String token = BaseService.getBaseService().getToken();
            	    if(token == null){
            	        // recreate session here
            	    	Log.d("token", token);
            	    }
            	} catch (BaseServiceException e) {
            	    e.printStackTrace();
            	}
                try {
                    chatService.startAutoSendPresence(AUTO_PRESENCE_INTERVAL_IN_SECONDS);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }

                // go to Dialogs screen
                //
                User_Id = user.getId();
//                Integer a = user.getId();
                Intent intent = new Intent(SplashActivity.this, DialogsActivity.class);
//                intent.putExtra("id", a);
                startActivity(intent);
               
//                finish();
            }

            @Override
            public void onError(List errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();
                Toast.makeText(getApplicationContext(),"chat login errors: " + errors.toString() ,Toast.LENGTH_LONG).show();
//                try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
            }
        });
    }
    public void onBackPressed(){
    	
    }
}