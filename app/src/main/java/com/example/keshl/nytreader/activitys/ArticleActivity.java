package com.example.keshl.nytreader.activitys;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.keshl.nytreader.Constants;
import com.example.keshl.nytreader.NetworkUtil;
import com.example.keshl.nytreader.R;
import com.example.keshl.nytreader.database.DBHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.keshl.nytreader.database.ArticleDbSchema.ArticleTable;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener{

    private WebView webView;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;
    private String title;
    private String pageHtml;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        progressBar = findViewById(R.id.progressBarArticle);

        initWebView();
        initFloatingActionButton();

        switch (getIntent().getExtras().getString(Constants.ARTICLE_ACTIVITY_CASE)){
            case Constants.OPEN_ARTICLE:
                downloadHtmlPage();
                break;
            case Constants.OPEN_DOWNLOAD_ARTICLE:
                showSavedPage();
                break;
        }

    }

    @Override
    public void onClick(View view) {
        ContentValues values = getContentValue();
        SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
        if(db.insert(ArticleTable.NAME,null, values)>0){
            showSnackbar("Article save");
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    private ContentValues getContentValue() {
        ContentValues values = new ContentValues();
        values.put(ArticleTable.Cols.TITLE, title);
        values.put(ArticleTable.Cols.HTML, pageHtml);
        return values;
    }

    private void initWebView() {
        webView = findViewById(R.id.webView);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
    }

    private void initFloatingActionButton() {
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
    }

    private void downloadHtmlPage() {
        if(NetworkUtil.isNetworkAvailable(this)){
            String articleUrl = getIntent().getExtras().getString(Constants.ARTICLE_URL);
            if (articleUrl!=null && !articleUrl.isEmpty()){
                new HtmlLoader().execute(articleUrl);
            }
            title = getIntent().getExtras().getString(Constants.ARTICLE_TITLE);
        }
        else {
            showSnackbar(this.getString(R.string.not_connected_internet));
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    private void showSavedPage() {
        floatingActionButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        title = getIntent().getExtras().getString(Constants.ARTICLE_TITLE);
        getHtmlInDb();

        showArticle();
    }

    private void getHtmlInDb() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(ArticleTable.NAME, new String[]{ArticleTable.Cols.HTML},
                ArticleTable.Cols.TITLE + " =  ?", new String[]{title}, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {

                do {
                    for (String cn : c.getColumnNames()) {
                        pageHtml = c.getString(c.getColumnIndex(cn));
                    }
                } while (c.moveToNext());
            }
            c.close();
            dbHelper.close();
        }
    }

    private void showArticle() {
        webView.loadData(pageHtml, "text/html; charset=utf-8",null);
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



    class HtmlLoader extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            URLConnection connection = null;
            Reader reader = null;
            String html = new String();
            try {
                url = new URL(params[0]);
                connection = url.openConnection();
                Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
                Matcher m = p.matcher(connection.getContentType());

                String charset = m.matches() ? m.group(1) : "ISO-8859-1";
                reader = new InputStreamReader(connection.getInputStream(), charset);

                StringBuilder htmlBuilder = new StringBuilder();
                while (true) {
                    int ch = 0;
                    try {
                        ch = reader.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (ch < 0)
                        break;
                    htmlBuilder.append((char) ch);
                }
                html = htmlBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return html;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pageHtml = result;

            progressBar.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);

            showArticle();
        }
    }

}
