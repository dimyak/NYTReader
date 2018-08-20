package com.example.keshl.nytreader.activitys;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.keshl.nytreader.Constants;
import com.example.keshl.nytreader.NetworkUtil;
import com.example.keshl.nytreader.R;

public class ArticleActivity extends AppCompatActivity {

    private WebView webView;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        progressBar = findViewById(R.id.progressBarArticle);

        initWebView();
        showArticle();
    }

    private void initWebView() {
        webView = findViewById(R.id.webView);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);

            }
        });
    }

    private void showArticle() {
        if (NetworkUtil.isNetworkAvailable(this)){
            String articleUrl = getIntent().getExtras().getString(Constants.ARTICLE_URL);
            if (articleUrl!=null&&!articleUrl.isEmpty()){
                webView.loadUrl(articleUrl);
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        } else {
            showSnackbar(this.getString(R.string.not_connected_internet));
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    private void showSnackbar(String message){
        Snackbar snackbar;
        snackbar = Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.RED);
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.show();
    }

}
