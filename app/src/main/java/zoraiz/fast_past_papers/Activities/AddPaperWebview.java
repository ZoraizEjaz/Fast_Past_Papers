package zoraiz.fast_past_papers.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import zoraiz.fast_past_papers.R;
import zoraiz.fast_past_papers.Utils.MyWebViewClient;

public class AddPaperWebview extends AppCompatActivity {
    WebView yt;
    ProgressBar pb, hb;
    String url = "";
    String activity_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paper_webview);


        try{
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }

        }catch (Exception ex){

        }


        url="http://fastnu.technocraz.net/upload-paper";
        activity_name = "Upload Paper";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(activity_name);
        setSupportActionBar(toolbar);

        yt = (WebView) findViewById(R.id.YTWeb);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        hb = (ProgressBar) findViewById(R.id.horizontalPb);

        if (isNetworkAvailable())
        {
            yt.loadUrl(url);
        }
        else
        {
            pb.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(toolbar, "No Internet Connection..!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(AddPaperWebview.this, MainActivity.class);
                            finish();
                            startActivity(i);
                        }
                    });
            snackbar.setActionTextColor(Color.CYAN);
            snackbar.show();
        }

        WebSettings webSettings = yt.getSettings();
        webSettings.setJavaScriptEnabled(true);
        yt.setWebViewClient(new MyWebViewClient());

        yt.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                hb.setProgress(newProgress);
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                    hb.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && yt.canGoBack()) {
            yt.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to Exit")

                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // showInterstitial();
                        finish();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  showInterstitial();
                        dialog.cancel();
                    }

                });

        android.support.v7.app.AlertDialog alert = builder.create();
        alert.setTitle("Exit ?");
        alert.show();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }






}