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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keshl.nytreader.Adapters.FavoritesRecyclerAdapter;
import com.example.keshl.nytreader.OnSavedRecyclerListener;
import com.example.keshl.nytreader.R;
import com.example.keshl.nytreader.database.ArticleDbSchema;
import com.example.keshl.nytreader.database.DBHelper;
import com.example.keshl.nytreader.model.ArticleDbModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.keshl.nytreader.database.ArticleDbSchema.ArticleTable;


public class FavoritesFragment extends Fragment implements OnSavedRecyclerListener {
    private View view;
    private List<ArticleDbModel> articleList = new ArrayList<>();
    private RecyclerView recyclerView;
    FavoritesRecyclerAdapter favoritesRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.most_popular_fragment, container, false);

        loadArticleDbList();
        initRecyclerView();
        initSwipeRefresh();

        return view;
    }

    private void loadArticleDbList() {
        SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();
        Cursor c = db.query(ArticleDbSchema.ArticleTable.NAME, null, null, null, null, null, null);
        articleList.clear();
        if (c.moveToFirst()) {

            int titleIndex = c.getColumnIndex(ArticleDbSchema.ArticleTable.Cols.TITLE);
            int htmlIndex = c.getColumnIndex(ArticleDbSchema.ArticleTable.Cols.HTML);

            do {
                ArticleDbModel article = new ArticleDbModel(c.getString(titleIndex), c.getString(htmlIndex));
                articleList.add(article);
            } while (c.moveToNext());
        }
        c.close();
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(articleList, getContext(), this);
        recyclerView.setAdapter(favoritesRecyclerAdapter);
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
        loadArticleDbList();
        favoritesRecyclerAdapter.notifyDataSetChanged();

    }

}
