package com.quickblox.sample.message;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.chat.ApplicationSingleton;
import com.quickblox.sample.chat.R;
import com.quickblox.sample.chat.core.PrivateChatManagerImpl;
import com.quickblox.sample.chat.ui.activities.ChatActivity;
import com.quickblox.sample.chat.ui.activities.DialogsActivity;
import com.quickblox.sample.chat.ui.activities.SplashActivity;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class GCMIntentService extends GCMBaseIntentService {
//public class GCMIntentService extends IntentService {
//
    public static final int NOTIFICATION_ID = 1;
//
    private static final String TAG = GCMIntentService.class.getSimpleName();
//
    private NotificationManager notificationManager;
    public final static long VIBRATOR_DURATION = 1000;
//    
//    public GCMIntentService() {
//        super("240590367572");
//    }
//
//   
//    
//    @Override
//	public void onHandleIntent(Intent intent) {
//        Log.i(TAG, "new push");
//        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
//        // The getMessageType() intent parameter must be the intent you received
//        // in your BroadcastReceiver.
//        String messageType = googleCloudMessaging.getMessageType(intent);
//        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
//            /*
//             * Filter messages based on message type. Since it is likely that GCM
//             * will be extended in the future with new message types, just ignore
//             * any message types you're not interested in, or that you don't
//             * recognize.
//             */
//            if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                processNotification("Send error: ", extras);
//            } else if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_DELETED.equals(messageType)) {
//                processNotification("Deleted messages on server: ", extras);
//                // If it's a regular GCM message, do some work.
//            } else if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                // Post notification of received message.
//                processNotification("Deleted messages on server: ", extras);
//                Log.i(TAG, "Received: " + extras.toString());
//            }
//        }
//        // Release the wake lock provided by the WakefulBroadcastReceiver.
//        GcmBroadcastReceiver.completeWakefulIntent(intent);
//    }
//
//    // Put the message into a notification and post it.
//    // This is just one simple example of what you might choose to do with
//    // a GCM message.
//    private void processNotification(String type, Bundle extras) {
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        final String messageValue = extras.getString("message");
//        
//        QBUser user = new QBUser();
//        QBChatMessage message = PrivateChatManagerImpl.Message;
//        Integer id = null;
//        try {
//			id = message.getSenderId();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//        if(id!=null){
//	        try {
//				user = QBUsers.getUser(id);
//			} catch (QBResponseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				user.setLogin("Server");
//			}
//        }else user.setLogin("Server");
//        
//        
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.putExtra("message", messageValue);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(
//                R.drawable.mess_for_push_notif).setContentTitle(user.getLogin()).setStyle(
//                new NotificationCompat.BigTextStyle().bigText(messageValue)).setContentText(messageValue)
//                .setVibrate(new long[]{0, VIBRATOR_DURATION})
//                .setSound(uri);
//        mBuilder.setAutoCancel(true);
//        mBuilder.setContentIntent(contentIntent);
//        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//    }
////    @Override
//    public static void onRegistered(Context context, final String registrationId) {
//     
//        // access to your acivity
//        if(ChatActivity.getInstance() != null){
//        	ChatActivity.getInstance().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                	ChatActivity.getInstance().retrieveMessage(registrationId);
//                }
//            });
//        }
//    }
//
////	@Override
////	protected void onError(Context arg0, String arg1) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	protected void onMessage(Context context, Intent intent) {
////		// TODO Auto-generated method stub
////	}
////
////	@Override
////	protected void onUnregistered(Context arg0, String arg1) {
////		// TODO Auto-generated method stub
////			
////	}


//public class GCMIntentService extends GCMBaseIntentService {

//private static final String TAG = "GCMIntentService";

public GCMIntentService() {
    super("240590367572");
}

/**
 * Method called on device registered
 **/
@Override
protected void onRegistered(Context context, final String registrationId) {

  // access to your acivity
  if(ChatActivity.getInstance() != null){
  	ChatActivity.getInstance().runOnUiThread(new Runnable() {
          @Override
          public void run() {
          	ChatActivity.getInstance().retrieveMessage(registrationId);
          }
      });
  }
}
/**
 * Method called on device un registred
 * */
@Override
protected void onUnregistered(Context context, String registrationId) {
    Log.i(TAG, "Device unregistered");
//    displayMessage(context, getString(R.string.gcm_unregistered));
//    ServerUtilities.unregister(context, registrationId);
}

/**
 * Method called on Receiving a new message
 * */
@Override
protected void onMessage(Context context, Intent intent) {
	Log.i(TAG, "new push");
  Bundle extras = intent.getExtras();
  GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
  // The getMessageType() intent parameter must be the intent you received
  // in your BroadcastReceiver.
  String messageType = googleCloudMessaging.getMessageType(intent);
  if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
      /*
       * Filter messages based on message type. Since it is likely that GCM
       * will be extended in the future with new message types, just ignore
       * any message types you're not interested in, or that you don't
       * recognize.
       */
      if (GoogleCloudMessaging.
              MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
          processNotification("Send error: ", extras);
      } else if (GoogleCloudMessaging.
              MESSAGE_TYPE_DELETED.equals(messageType)) {
          processNotification("Deleted messages on server: ", extras);
          // If it's a regular GCM message, do some work.
      } else if (GoogleCloudMessaging.
              MESSAGE_TYPE_MESSAGE.equals(messageType)) {
          // Post notification of received message.
          processNotification("Deleted messages on server: ", extras);
          Log.i(TAG, "Received: " + extras.toString());
      }
  }
  // Release the wake lock provided by the WakefulBroadcastReceiver.
  GcmBroadcastReceiver.completeWakefulIntent(intent);
}


/**
 * Method called on Error
 * */
@Override
public void onError(Context context, String errorId) {
    Log.i(TAG, "Received error: " + errorId);
//    displayMessage(context, getString(R.string.gcm_error, errorId));
}

//@Override
//protected boolean onRecoverableError(Context context, String errorId) {
//    // log message
//    Log.i(TAG, "Received recoverable error: " + errorId);
//    displayMessage(context,
//            getString(R.string.gcm_recoverable_error, errorId));
//    return super.onRecoverableError(context, errorId);
//}

/**
 * Issues a notification to inform the user that server has sent a message.
 */

private void processNotification(String type, Bundle extras) {
  notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

  final String messageValue = extras.getString("message");
  
  QBUser user = new QBUser();
  QBChatMessage message = PrivateChatManagerImpl.Message;
  Integer id = null;
  try {
		id = message.getSenderId();
	} catch (Exception e) {
		// TODO: handle exception
	}
  if(id!=null){
      try {
			user = QBUsers.getUser(id);
		} catch (QBResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user.setLogin("Server");
		}
  }else user.setLogin("Server");
  
  
  Intent intent = new Intent(this, SplashActivity.class);
  intent.putExtra("message", messageValue);
  PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
  Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
  NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(
          R.drawable.mess_for_push_notif).setContentTitle(user.getLogin()).setStyle(
          new NotificationCompat.BigTextStyle().bigText(messageValue)).setContentText(messageValue)
          .setVibrate(new long[]{0, VIBRATOR_DURATION})
          .setSound(uri);
  mBuilder.setAutoCancel(true);
  mBuilder.setContentIntent(contentIntent);
  notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
}
	
}