package com.example.keshl.nytreader.fragments;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keshl.nytreader.Constants;
import com.example.keshl.nytreader.NYTApi;
import com.example.keshl.nytreader.NetworkUtil;
import com.example.keshl.nytreader.R;
import com.example.keshl.nytreader.Adapters.MostPopularRecyclerAdapter;
import com.example.keshl.nytreader.RetrofitInstance;
import com.example.keshl.nytreader.model.ResponceList;
import com.example.keshl.nytreader.model.ResultsItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseMostPopularFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private RecyclerView recyclerView;
    private TextView emptyList;
    private MostPopularRecyclerAdapter mostPopularRecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ResultsItem> resultsItems = new ArrayList<>();
    private boolean firstDownloadСompleted = false;
    private boolean refresh = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout, container, false);
        emptyList = view.findViewById(R.id.textViewEmptyList);
        initFragment();
        downloadDate();
        return view;
    }

    @Override
    public void onRefresh() {
        refresh = true;
        downloadDate();
    }

    private void initFragment(){
        initRecyclerView();
        initSwipeRefresh();
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        mostPopularRecyclerAdapter = new MostPopularRecyclerAdapter(resultsItems,getContext());
        recyclerView.setAdapter(mostPopularRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void downloadDate() {
        if(!firstDownloadСompleted ||refresh){
            if(NetworkUtil.isNetworkAvailable(getContext())){
                startDownload();
            }else {

                if(!firstDownloadСompleted)
                   showEmptyView();

                swipeRefreshLayout.setRefreshing(false);
                refresh = false;
                showSnackbar(getContext().getString(R.string.not_connected_internet));
            }
        }
    }

    private void startDownload() {
        NYTApi api = RetrofitInstance.getRetrofitInstance().create(NYTApi.class);
        Call<ResponceList> call = getDate(api);

        call.enqueue(new Callback<ResponceList>() {
            @Override
            public void onResponse(Call<ResponceList> call, Response<ResponceList> response) {
                if(response.isSuccessful()){
                    ResponceList responceList = response.body();
                    if(responceList!=null){

                        resultsItems.clear();
                        resultsItems.addAll(responceList.getResults());

                        mostPopularRecyclerAdapter.notifyDataSetChanged();

                       // Toast.makeText(getContext(),"Download",Toast.LENGTH_SHORT).show();
                        firstDownloadСompleted = true;
                        hideEmptyView();
                    }
                }
                else {
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                refresh = false;
            }

            @Override
            public void onFailure(Call<ResponceList> call, Throwable t) {
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                refresh = false;
            }
        });
    }

    //need to Override in child Fragments
    protected Call<ResponceList> getDate(NYTApi api){
        return api.getMostSharedDate(Constants.SECTION,Constants.TIME_PERIOD, Constants.API_KEY);
    }

    private void showEmptyView(){
        emptyList.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideEmptyView(){
        emptyList.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showSnackbar(String message){
        Snackbar snackbar;
        snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
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
