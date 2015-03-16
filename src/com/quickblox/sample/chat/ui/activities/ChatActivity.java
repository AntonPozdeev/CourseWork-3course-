package com.quickblox.sample.chat.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatHistoryMessage;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBMessage;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.result.Result;
import com.quickblox.messages.QBMessages;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;

import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.sample.chat.ApplicationSingleton;
import com.quickblox.sample.chat.R;
import com.quickblox.sample.chat.core.ChatManager;
import com.quickblox.sample.chat.core.GroupChatManagerImpl;
import com.quickblox.sample.chat.core.PrivateChatManagerImpl;
import com.quickblox.sample.chat.ui.adapters.ChatAdapter;



import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;


import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_DIALOG = "dialog";
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";

    public static EditText messageEditText;
    private ListView messagesContainer;
    public static Button sendButton;
    private ProgressBar progressBar;

    private Mode mode = Mode.PRIVATE;
    private ChatManager chat;
    private ChatAdapter adapter;
    private QBDialog dialog;

    
    private ArrayList<QBChatHistoryMessage> history;

	private boolean flag=true;

	private boolean connectFlag=true;
	////////////////
	private static ChatActivity instance;
	public static ChatActivity getInstance() {
        return instance;
    }
	//////////////////////
	
    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public void retrieveMessage(final String message) {
        String text = message + "\n\n" + messageEditText.getText().toString();
        messageEditText.setText(text);
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
    }
    
//    @Override
//    public void onBackPressed() {
//        try {
//            chat.release();
//        } catch (XMPPException e) {
//            Log.e(TAG, "failed to release chat", e);
//        }
//        super.onBackPressed();
//    }

    private void initViews() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageEditText = (EditText) findViewById(R.id.messageEdit);
        sendButton = (Button) findViewById(R.id.chatSendButton);
        
        TextView meLabel = (TextView) findViewById(R.id.meLabel);
        TextView companionLabel = (TextView) findViewById(R.id.companionLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();

        // Get chat dialog
        //
        try{
        	dialog = (QBDialog)intent.getSerializableExtra(EXTRA_DIALOG);
        }
        catch(Exception e){
        	
        }
        mode = (Mode) intent.getSerializableExtra(EXTRA_MODE);
        switch (mode) {
            case GROUP:
                chat = new GroupChatManagerImpl(this);
                container.removeView(meLabel);
                container.removeView(companionLabel);

                // Join group chat
                //
                progressBar.setVisibility(View.VISIBLE);
                //
                ((GroupChatManagerImpl) chat).joinGroupChat(dialog, new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {

                        // Load Chat history
                        //
                        loadChatHistory();
                    }

                    @Override
                    public void onError(List list) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                        dialog.setMessage("error when join group chat: " + list.toString()).create().show();
                    }
                });

                break;
            case PRIVATE:
                Integer opponentID = ((ApplicationSingleton)getApplication()).getOpponentIDForPrivateDialog(dialog);
                //при создании нового диалога,если пользователь хочет создать диалог с самим собой,то следующий код
                // не разрешает это сделать
                if(opponentID==-1){
                	
//                	Intent intent1 = new Intent(ChatActivity.this, NewDialogActivity.class);
//                	startActivity(intent1);
//                	finish();
                	
                	return;
}
                //
                	chat = new PrivateChatManagerImpl(this, opponentID);
                
                //следующий код сделан дл€ того,чтобы загружать диалог и просмотривать его,если собеседник удалилс€
                //если flag = false, то в диалоге где нет собеседника(он удалилс€) нельз€ будет написать и отправить
                //сообщени€
                try{
                	companionLabel.setText(((ApplicationSingleton)getApplication()).getDialogsUsers().get(opponentID).getLogin());
                }
                catch (Exception e) {
                	flag = false;
                	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
				}

                
                // Load CHat history
                //
//                if(connectFlag==true){
                	loadChatHistory();
//                	break;
//                }
//                else Toast.makeText(getApplicationContext(), "ѕроверь подключение к интернету", Toast.LENGTH_LONG).show();
        }
        if(flag){
        		sendButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	                String messageText = messageEditText.getText().toString();
	                if (TextUtils.isEmpty(messageText)) {
	                    return;
	                }
	               
	                // Send chat message
	                //
//	                if(NetworkChangeReceiver.fla==true){
	                	QBChatMessage chatMessage = new QBChatMessage();
	                	chatMessage.setBody(messageText);
	                	chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
	
	                	try {
	                		chat.sendMessage(chatMessage);
	                		
	                	} catch (XMPPException e) {
	                		Log.e(TAG, "failed to send a message", e);
	                	} catch (SmackException sme){
	                		Log.e(TAG, "failed to send a message", sme);
	                	}catch (Exception ex){
	                		Toast.makeText(getApplicationContext(),
	                				"—ообщение не может быть отправлено, так как интернет отключЄн" + ex.toString(),
	                				Toast.LENGTH_LONG).show();
	                	}
	                	StringifyArrayList<Integer> userId = new StringifyArrayList<Integer>();
	                	Integer opponentID = ((ApplicationSingleton)getApplication()).getOpponentIDForPrivateDialog(dialog);
	                    userId.add(opponentID);
	                    
	                    //Using PUSH notification
	                    QBEvent event = new QBEvent();
	                    event.setUserIds(userId);
	                    event.setEnvironment(QBEnvironment.DEVELOPMENT);
	                    event.setNotificationType(QBNotificationType.PUSH);
	                    
	                    event.setMessage(messageText);

	                    QBMessages.createEvent(event, new QBEntityCallbackImpl<QBEvent>() {
	                    	@Override
	                        public void onSuccess(QBEvent qbEvent, Bundle args) {
	                            System.out.println("GCM Message Sent inside event " );
	                            Toast.makeText(getApplicationContext(),"Sended", Toast.LENGTH_LONG).show();
	                        }

	                        @Override
	                        public void onError(List<String> errors) {
	                            System.out.println("GCM Message ERROR inside event ");
	                            Toast.makeText(getApplicationContext(),"Not sended:" + errors.toString(), Toast.LENGTH_LONG).show();
	                        }
	                        
	                    });
	                	messageEditText.setText("");
	
	                	if(mode == Mode.PRIVATE) {
	                		showMessage(chatMessage);
	                	}
	                }
//	                else Toast.makeText(getApplicationContext(), "—ообщение не может быть отправлено, так как интернет отключЄн"
//	                		, Toast.LENGTH_LONG).show();
//	            }
	        });
        }
        else{
//        	 свойства Focusable, Long clickable и Cursor visible.
        	messageEditText.setFocusable(false);
        	messageEditText.setLongClickable(false);
        	messageEditText.setCursorVisible(false);
        	sendButton.setEnabled(false);
        }
    }

    
    private void loadChatHistory(){
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setPagesLimit(100);

        QBChatService.getDialogMessages(dialog, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBChatHistoryMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatHistoryMessage> messages, Bundle args) {
            	messageEditText.setFocusable(true);
            	messageEditText.setLongClickable(true);
            	messageEditText.setCursorVisible(true);
            	sendButton.setEnabled(true);
                history = messages;

                adapter = new ChatAdapter(ChatActivity.this, new ArrayList<QBMessage>());
                messagesContainer.setAdapter(adapter);
                for(QBMessage msg : messages) {
                    showMessage(msg);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                dialog.setMessage("load chat history errors: " + errors).create().show();
                messageEditText.setFocusable(false);
            	messageEditText.setLongClickable(false);
            	messageEditText.setCursorVisible(false);
            	sendButton.setEnabled(false);
            }
        });
    }

    public void showMessage(QBMessage message) {
        adapter.add(message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                scrollDown();
            }
        });
    }

    private void scrollDown() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    public static enum Mode {PRIVATE, GROUP}
    
    public void onBackPressed() {
       Intent intent = new Intent(ChatActivity.this, DialogsActivity.class);
       startActivity(intent);
       finish();
    }

}
