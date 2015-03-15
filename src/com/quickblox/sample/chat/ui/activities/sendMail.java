package com.quickblox.sample.chat.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class sendMail extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
	}
	 protected void onResume() {
	    	super.onResume();
	    	Mail m = new Mail("pozdeev1994@gmail.com", "30111994axel"); 
	        String[] toArr = {"anton-pozdeev@mail.ru"}; 
	        m.setTo(toArr); 
	        m.setFrom("anton-pozdeev1994@mail.ru"); 
	        m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
	        m.setBody("Email bodyrrrrr"); 
	        try { 
//	          m.addAttachment("/sdcard/bday.jpg"); 
	          if(m.send()) { 
	            Toast.makeText(this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
	          } else { 
	            Toast.makeText(this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
	          } 
	        } catch(Exception e) { 
	          Log.e("MailApp", "Could not send email", e); 
	          Toast.makeText(this, "Could not send email" + e.toString(), Toast.LENGTH_LONG).show(); 
	        } 
	    }
}
