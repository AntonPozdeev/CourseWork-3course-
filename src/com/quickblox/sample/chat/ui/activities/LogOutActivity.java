package com.quickblox.sample.chat.ui.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.sample.chat.R;
import com.quickblox.users.QBUsers;

public class LogOutActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_activity);
        
        final QBChatService chatService = QBChatService.getInstance();
    	
        try{
        	chatService.logout(new QBEntityCallbackImpl() {
				 
			    @Override
			    public void onSuccess() {
			        // success
			        chatService.destroy();
			    }
			    
			    @Override
			    public void onError(final List list) {
			    	Toast.makeText(getApplicationContext(),"Error with destroy" + list.toString(),
			    			Toast.LENGTH_SHORT).show();
			    }
			});
    	}
    	catch(Exception ex){
    		Toast.makeText(getApplicationContext(),"Ошибка при destroy:" + ex.toString(),Toast.LENGTH_LONG).show();
    		return;
    	}
    	try{
			QBUsers.signOut(new QBEntityCallbackImpl(){
			    public void onSuccess() {
			    	Toast.makeText(getApplicationContext(),"Success" ,Toast.LENGTH_SHORT).show();
			    	Intent intent2 = new Intent(LogOutActivity.this, SplashActivity.class);
					startActivity(intent2);
					finish();
			    }
			    public void onError(List errors) {
			    	Toast.makeText(getApplicationContext(), errors.toString(),Toast.LENGTH_SHORT).show();
			    }
			});
    	}
    	catch(Exception e){
    		Toast.makeText(getApplicationContext(),"Ошибка при SignOut:" + e.toString(),Toast.LENGTH_LONG).show();
	}
	}
}
