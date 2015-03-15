//package com.quickblox.sample.chat.ui.activities;
//
//import com.perm.kate.api.Auth;
//import com.quickblox.sample.chat.R;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.util.Log;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//public class vk extends Activity{
//WebView webview;
//private static final String TAG = "Kate.LoginActivity";
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//        
//        webview = (WebView) findViewById(R.id.vkontakteview);
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.clearCache(true);
//        
//        //����� �������� ����������� �� ��������� �������� ��������
//        webview.setWebViewClient(new VkontakteWebViewClient());
//                
//        //otherwise CookieManager will fall with java.lang.IllegalStateException: CookieSyncManager::createInstance() needs to be called before CookieSyncManager::getInstance()
//        CookieSyncManager.createInstance(this);
//        
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeAllCookie();
//        
//        String url=Auth.getUrl("4810200", Auth.getSettings());
//        webview.loadUrl(url);
//    }
//    
//    class VkontakteWebViewClient extends WebViewClient {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            parseUrl(url);
//        }
//    }
//    
//    private void parseUrl(String url) {
//        try {
//            if(url==null)
//                return;
//            Log.i(TAG, "url="+url);
//            if(url.startsWith(Auth.redirect_url))
//            {
//                if(!url.contains("error=")){
//                    String[] auth=Auth.parseRedirectUrl(url);
//                    Intent intent=new Intent();
//                    intent.putExtra("token", auth[0]);
//                    intent.putExtra("user_id", Long.parseLong(auth[1]));
//                    setResult(Activity.RESULT_OK, intent);
//                }
//                finish();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
