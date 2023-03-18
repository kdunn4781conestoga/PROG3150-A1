package ca.kdunn4781.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class Shop extends AppCompatActivity {

    private WebView webForShop = null;
    private ProgressBar progressBarForWeb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        webForShop = findViewById(R.id.webView);
        progressBarForWeb = findViewById(R.id.webProgressBar);

        webForShop.getSettings().setJavaScriptEnabled(true);

        // METHOD     : shouldOverrideUrlLoading
        // PARAMETER  : WebView onlineInvitationWeb, String url
        // RETURN     : true or false
        //DESCRIPTION : overides the URL
        webForShop.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView onlineInvitationWeb, String url) {
                return false;
            }
        });


        // METHOD     : onProgressChanged
        // PARAMETER  : WebView onlineInvitationWeb, int progress
        // RETURN     : void
        //DESCRIPTION : progress bar
        webForShop.setWebChromeClient( new WebChromeClient() {
            public void onProgressChanged(WebView onlineInvitationWeb, int progress) {
                if (progress >= 100)  {
                    progressBarForWeb.setVisibility(View.GONE);
                } else {
                    progressBarForWeb.setVisibility(View.INVISIBLE);
                }
            }
        });

        webForShop.loadUrl("https://amazon.ca/");

    }
}