package com.quickblox.sample.chat.ui.activities;

import javax.mail.search.IntegerComparisonTerm;

import com.quickblox.sample.chat.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationAct extends Activity{
	private EditText fullNameEditText;
	private EditText emailEditText;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private EditText phoneEditText;
    private Button	 signup;
    private TextView errorWithEmail;
    private TextView errorWithLogin;
    private TextView errorWithPassword;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    initUI();
    
    
    signup.setOnClickListener(new View.OnClickListener() {
    	public void onClick(View view) {
    		boolean flag=true;
    		final String FullName = fullNameEditText.getText().toString();
    		final String Email = emailEditText.getText().toString();
    		final String LogIn = loginEditText.getText().toString();
			final String Pswd = passwordEditText.getText().toString();
			String confirm = confirmEditText.getText().toString();
			final String Phone = phoneEditText.getText().toString();
	        if(Email.equals("")){
	        	errorWithEmail.setVisibility(View.VISIBLE);
	        	flag = false;
	        }
	        else {
	        	errorWithEmail.setVisibility(View.INVISIBLE);
	        	flag = true;
	        }
	        
	        if(LogIn.isEmpty()){
	        		errorWithLogin.setVisibility(View.VISIBLE);
	        		flag = false;
	        }
	        else {
	        	errorWithLogin.setVisibility(View.INVISIBLE);
	        	flag = true;
	        }
	        
	        if(LogIn.length()<2){
	        	errorWithLogin.setVisibility(View.VISIBLE);
        		flag = false;
	        }
	        else {
	        	errorWithLogin.setVisibility(View.INVISIBLE);
	        	flag = true;
	        }	        
	        
	        if(Pswd.length()<8){
	        	errorWithPassword.setVisibility(View.VISIBLE);
        		flag = false;
	        }
	        else {
	        	errorWithPassword.setVisibility(View.INVISIBLE);
	        	flag = true;
	        }
	        
	        //Использую .isEmpty() в качесте проверки на пустоту,говорят что этот метод работает быстрее,
	        //чем equals("")
	        if(Pswd.isEmpty() || !Pswd.equals(confirm) || Pswd.length()<8){
	        		errorWithPassword.setVisibility(View.VISIBLE);
	        		flag = false;
	        }
	        else {
	        	errorWithPassword.setVisibility(View.INVISIBLE);
	        	flag = true;
	        }
	        
			if(!flag)
	        	return;
			else{
				
		        Intent result = new Intent();
	            result.putExtra("fullname", FullName);
	            result.putExtra("email", Email);
	            result.putExtra("login", LogIn);
	            result.putExtra("password",Pswd );
	            result.putExtra("phone", Phone);
	            setResult(RESULT_OK,result);
	            finish();				
			}
    	}
    	});
    }
    
    public void onBackPressed() {
        Intent intent = new Intent(RegistrationAct.this, SplashActivity.class);
        startActivity(intent);
        finish();
     }
    
			private void initUI() {
				fullNameEditText = (EditText)findViewById(R.id.fullNameEdit);
				emailEditText = (EditText)findViewById(R.id.emailEdit);
				loginEditText = (EditText) findViewById(R.id.loginEdit);
			    passwordEditText = (EditText) findViewById(R.id.passwordEdit);
			    confirmEditText = (EditText)findViewById(R.id.confirmEdit);
			    phoneEditText = (EditText)findViewById(R.id.phoneEdit);
			    
			    signup = (Button)findViewById(R.id.RegistrationButton);
			    errorWithEmail = (TextView)findViewById(R.id.EmailViewError);
			    errorWithLogin = (TextView)findViewById(R.id.LoginViewError);
			    errorWithPassword = (TextView)findViewById(R.id.PswdCnfrViewError);
			}
}
    
