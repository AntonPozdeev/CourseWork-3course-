package com.quickblox.sample.chat.ui.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemSelectedListener;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.sample.chat.R;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class LoginAndPswdActivity extends Activity implements OnItemSelectedListener {
	
	EditText login;
	EditText pswd;
	Button bLogin;
	Button signUp;
	TextView resetPassword;
	TextView loginPswdError;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		init();

		//вход
		bLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String logIn = login.getText().toString();
				String Pswd = pswd.getText().toString();
				
				if(logIn.equals("") || Pswd.equals("") || logIn.length()<2 || Pswd.length()<8){
					loginPswdError.setVisibility(View.VISIBLE);
					return;
				}
				else loginPswdError.setVisibility(View.INVISIBLE);				
				Intent result = new Intent();
				result.putExtra("login", logIn);
				result.putExtra("password", Pswd);
				setResult(RESULT_OK,result);				
				finish();
			}
		});
		//регистраци€
		signUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginAndPswdActivity.this, RegistrationActivity.class);
				startActivity(intent);
				finish();
			}
		});	
		
		//забыли пароль
		resetPassword.setText (Html.fromHtml ("<FONT COLOR = Blue > <U>Do you forgot your password?</ U> </ FONT>"));
		resetPassword.setMovementMethod (LinkMovementMethod.getInstance ());
		resetPassword.setOnClickListener(new View. OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final EditText input = new EditText(LoginAndPswdActivity.this);
//				final EditText input = new EditText(RegistrationActivity.this);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginAndPswdActivity.this);
	            builder.setTitle("Control")
						.setMessage("Write your email:")
						.setView(input)// ƒобавим поле ввода
	                    .setNeutralButton("Confirm",
	                            new DialogInterface.OnClickListener() {
	                                private QBChatService chatService;

									public void onClick(DialogInterface dialog,
	                                        int id) {
										QBSettings.getInstance().fastConfigInit(SplashActivity.APP_ID, SplashActivity.AUTH_KEY, SplashActivity.AUTH_SECRET);
	                                    if (!QBChatService.isInitialized()) {
	                                        QBChatService.init(LoginAndPswdActivity.this);
	                                    }
	                                    chatService = QBChatService.getInstance();
	                                	final QBUser user = new QBUser();
	                                	QBAuth.createSession(user,  new QBEntityCallbackImpl<QBSession>(){
	                                		 @Override
	                                         public void onSuccess(QBSession session, Bundle args) {
	                                			 QBUsers.resetPassword(input.getText().toString(), new QBEntityCallbackImpl() {
	     	                                	    @Override
	     	                                	    public void onSuccess() {
	     	                                	    	Toast.makeText(getApplicationContext(), "Message send successful", Toast.LENGTH_LONG).show();
	     	                                	    }
	     	                                	 
	     	                                	    @Override
	     	                                	    public void onError(List errors) {
	     	                                	    	Log.d("reset password", errors.toString());
	     	                                	    	Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
	     	                                	    }
	     	                                	});
	                                         }

	                                         @Override
	                                         public void onError(List<String> errors) {
	                                        	 Log.d("reset password", errors.toString());
  	                                	    	Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
	                                         }
	                                     });
	                                	
	                                }
									}).create().show();
	            }
		});
	}
	
	private void init() {
		// TODO Auto-generated method stub
		login = (EditText)findViewById(R.id.loginEdit);
		pswd = (EditText)findViewById(R.id.passwordEdit);
		bLogin = (Button)findViewById(R.id.loginButton);
		signUp = (Button)findViewById(R.id.SignUpButton);
		resetPassword = (TextView)findViewById(R.id.forgotPasswordView); 
		loginPswdError = (TextView)findViewById(R.id.LoginPswdViewError);
	}

	public void onSaveInstanceState(Bundle savedInatanceState){
		
		super.onSaveInstanceState(savedInatanceState);
	}
    
    @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (data == null) {return;}
		  
	  }
    
	@Override
	public void onItemSelected(AdapterViewCompat<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNothingSelected(AdapterViewCompat<?> arg0) {
		// TODO Auto-generated method stub
		
	}
//	public QBUser login(String socialProvider, String accessToken,
//			  String accessTokenSecret) throws QBResponseException, BaseServiceException {
//			  QBUser user;
//			 
//			  QBSession session = QBAuth.createSession();
//			 
//			  user = QBUsers.signInUsingSocialProvider(socialProvider, accessToken, accessTokenSecret);
//			  user.setPassword(session.getToken());
//			 
//			  String token = QBAuth.getBaseService().getToken();
//			 
//			  AppSession.startSession(LoginType.FACEBOOK, user, token);
//			 
//			  return user;
//			}
	//выход из приложени€!!
	//удалить эту строчку как сможешь сделать правильный выход!
	public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("¬ыйти из приложени€?")
            .setMessage("¬ы действительно хотите выйти?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                     //SomeActivity - им€ класса Activity дл€ которой переопредел€ем onBackPressed(); 
                	finish();
                     System.exit(0);
                }
            }).create().show();
    }		
}
