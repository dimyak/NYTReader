package com.example.keshl.nytreader.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keshl.nytreader.Adapters.FavoritesRecycleAdapter;
import com.example.keshl.nytreader.OnSavedRecycleListener;
import com.example.keshl.nytreader.R;
import com.example.keshl.nytreader.database.ArticleDbSchema;
import com.example.keshl.nytreader.database.DBHelper;
import com.example.keshl.nytreader.model.ArticleDbModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.keshl.nytreader.database.ArticleDbSchema.ArticleTable;


public class FavoritesFragment extends Fragment implements OnSavedRecycleListener{
    private View view;
    private List<ArticleDbModel> articleList = new ArrayList<>();
    private RecyclerView recyclerView;
    FavoritesRecycleAdapter favoritesRecycleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout, container, false);


        loadArticleDbList();
        initRecyclerView();
        initSwipeRefresh();

        return view;
    }

    //Needs to refactor
    private void loadArticleDbList() {
        SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();

        Cursor c = db.query( ArticleDbSchema.ArticleTable.NAME, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int titleInedx = c.getColumnIndex( ArticleDbSchema.ArticleTable.Cols.TITLE);
            int htmlInedx = c.getColumnIndex( ArticleDbSchema.ArticleTable.Cols.HTML);
            articleList.clear();
            do {
                ArticleDbModel article = new ArticleDbModel(c.getString(titleInedx),c.getString(htmlInedx));

                articleList.add(article);
            } while (c.moveToNext());
        } else
            Log.d("HTML", "0 rows");
        c.close();
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        favoritesRecycleAdapter = new FavoritesRecycleAdapter(articleList, getContext(),this);
        recyclerView.setAdapter(favoritesRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initSwipeRefresh() {
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void deleteItem(int position) {
        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(ArticleTable.NAME,
                ArticleTable.Cols.TITLE + "=?",
                        new String[]{articleList.get(position).getTitle()});

        articleList.remove(position);
        favoritesRecycleAdapter.notifyDataSetChanged();

    }
}
