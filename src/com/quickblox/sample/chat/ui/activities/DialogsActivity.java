package com.quickblox.sample.chat.ui.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputBinding;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.sample.chat.ApplicationSingleton;
import com.quickblox.sample.chat.R;
import com.quickblox.sample.chat.ui.adapters.DialogsAdapter;
import com.quickblox.sample.message.GCMIntentService;
import com.quickblox.sample.chat.core.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.filter.PacketFilter;

public class DialogsActivity extends Activity{

    private ListView dialogsListView;
    private ProgressBar progressBar;
    String usId;
    boolean a;
	public static boolean flagg=true;
//    private Button logout;
//    private QBChatService chatService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogs_activity);
        
//        ActionBar bar = getActionBar();
//        bar.setDisplayHomeAsUpEnabled(true);
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        dialogsListView = (ListView) findViewById(R.id.roomsList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        logout = (Button)findViewById(R.id.logoutButton);
//        final boolean Islogout = true;
//        logout.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {				
//				Intent intent = new Intent(DialogsActivity.this, SplashActivity.class);
//				intent.putExtra("isLogout", Islogout);
//				startActivity(intent);
//				finish();
//			}
//        });
        // get dialogs
        //
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setPagesLimit(100);

        QBChatService.getChatDialogs(null, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(final ArrayList<QBDialog> dialogs, Bundle args) {

                // collect all occupants ids
                //
                List<Integer> usersIDs = new ArrayList<Integer>();
                for(QBDialog dialog : dialogs){
                    usersIDs.addAll(dialog.getOccupants());
                }

                // Get all occupants info
                //
                QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
                requestBuilder.setPage(1);
                requestBuilder.setPerPage(usersIDs.size());
                //
                QBUsers.getUsersByIDs(usersIDs, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle params) {

                        // Save users
                        //
                        ((ApplicationSingleton)getApplication()).setDialogsUsers(users);

                        // build list view
                        //
                        buildListView(dialogs);
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DialogsActivity.this);
                        dialog.setMessage("get occupants errors: " + errors).create().show();
                    }

                });
            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DialogsActivity.this);
                dialog.setMessage("get dialogs errors: " + errors).create().show();
                flagg = false;
                
            }
        });
    }

    
    void buildListView(List<QBDialog> dialogs){
        final DialogsAdapter adapter = new DialogsAdapter(dialogs, DialogsActivity.this);
        dialogsListView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

        // choose dialog
        //
        //��� alertdialog(��� ��������� ������� �� �������)
        dialogsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
						@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int posit, long arg3) {
							showAlertDialog(v,posit,adapter);
				return false;
			}
		});
        dialogsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBDialog selectedDialog = (QBDialog)adapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable(ChatActivity.EXTRA_DIALOG, (QBDialog)adapter.getItem(position));

                // group
                if(selectedDialog.getType().equals(QBDialogType.GROUP)){
                    bundle.putSerializable(ChatActivity.EXTRA_MODE, ChatActivity.Mode.GROUP);

                // private
                } else {
                    bundle.putSerializable(ChatActivity.EXTRA_MODE, ChatActivity.Mode.PRIVATE);
                }

                // Open chat activity
                //
                ChatActivity.start(DialogsActivity.this, bundle);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
    	getMenuInflater().inflate(R.menu.proba, menu);
        return super.onCreateOptionsMenu(menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
        switch (item.getItemId()) {
		case R.id.newchatMenu:
//			if(NetworkChangeReceiver.fla==true){
				Intent intent = new Intent(DialogsActivity.this, NewDialogActivity.class);
				try{
					startActivity(intent);
				}
				catch(Exception ex){
					Toast.makeText(getApplicationContext(),
							"������ ��� �������� �� �������� ������ ����:" + ex.toString(),
			    			Toast.LENGTH_SHORT).show();
				}
				finish();
//			}
//			else{}
			break;
		case R.id.SettingsMenu:
//			if(NetworkChangeReceiver.fla==true){
				Intent intent1 = new Intent(DialogsActivity.this, SettingsActivity.class);
				startActivity(intent1);
				finish();
//			}
//			else{}
				  
			break;
		case R.id.logoutMenu:
//			if(NetworkChangeReceiver.fla==true){
			
		        new AlertDialog.Builder(this)
		            .setTitle("Exiting account")
		            .setMessage("Do you want exiting account?")
		            .setNegativeButton(android.R.string.no, null)
		            .setPositiveButton(android.R.string.yes, new OnClickListener() {
		                public void onClick(DialogInterface arg0, int arg1) {
		                	Intent intent = new Intent(DialogsActivity.this, LogOutActivity.class);
		                	startActivity(intent);
		                	finish();
		                }}).create().show();
//		    }
//			else{}
			break;
		case R.id.exitMenu:
			new AlertDialog.Builder(this)
            .setTitle("����� �� ����������?")
            .setMessage("�� ������������� ������ �����?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                     //SomeActivity - ��� ������ Activity ��� ������� �������������� onBackPressed(); 
                     DialogsActivity.super.onBackPressed();
                     moveTaskToBack(true);

//                     super.onDestroy();

                     System.runFinalizersOnExit(true);
                     System.exit(0);
                }
            }).create().show();
			break;
		case R.id.removeMenu:
//			if(NetworkChangeReceiver.fla==true){
				boolean d = false;
				final EditText input = new EditText(this);
				input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				new AlertDialog.Builder(this)
				.setTitle("Control")
				.setMessage("Write your password for confirm:")
				// ������� ���� �����
				.setView(input)
				
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  String value = input.getText().toString();
				  // �������� �������� ��������� ������!
				  if(value.equals(SplashActivity.USER_PASSWORD)){
					  new AlertDialog.Builder(DialogsActivity.this)
				        .setTitle("delete dialog")
				        .setMessage("Do you really want delete yourth user?")
				        .setNegativeButton(android.R.string.no, null)
				        .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface arg0, int arg1) {
				            	QBUsers.deleteUser(SplashActivity.User_Id, new QBEntityCallbackImpl() {
	
				                    @Override
				                    public void onSuccess() {
				                      Intent intent = new Intent(DialogsActivity.this,SplashActivity.class);
				                      startActivity(intent);
				                      finish();
				                    }
	
				                    @Override
				                    public void onError(List errors) {
				                        Toast.makeText(getApplicationContext(), errors.toString(),Toast.LENGTH_SHORT).show();
				                    }
				                });
				            }}).create().show();
				  }
				}})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // ���� ��������.
				  }
				}).create().show();		
//			}
//			else{}
			break;
		case R.id.support:
			Intent intent3 = new Intent(DialogsActivity.this, SupportActivity.class);
			startActivity(intent3);
			finish();
			break;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("����� �� ����������?")
            .setMessage("�� ������������� ������ �����?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                     //SomeActivity - ��� ������ Activity ��� ������� �������������� onBackPressed(); 
                     DialogsActivity.super.onBackPressed();
                     finish();
                     moveTaskToBack(true);

//                     super.onDestroy();

                     System.runFinalizersOnExit(true);
                     System.exit(0);
                }
            }).create().show();
    }
    
    private void showAlertDialog(View v, final int position,final DialogsAdapter adapter){
    	new AlertDialog.Builder(this)
        .setTitle("delete dialog")
        .setMessage("Do you really want delete this dialog")
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	QBDialog selectedDialog = (QBDialog)adapter.getItem(position);
            	String dialogId = selectedDialog.getDialogId().toString();
            	 QBGroupChatManager.deleteDialog(dialogId, new QBEntityCallbackImpl<Void>() {
             	    @Override
             	    public void onSuccess() {
             	 
             	    }
             	 
             	    @Override
             	    public void onError(List<String> errors) {
             	 
             	    }
             	});
            }
        }).create().show();
    }
}
