package com.example.keshl.nytreader.fragments;

import com.example.keshl.nytreader.Constants;
import com.example.keshl.nytreader.NYTApi;
import com.example.keshl.nytreader.model.ResponceList;

import retrofit2.Call;

public class MostEmailedFragment extends BaseMostPopularFragment {

    @Override
    protected Call<ResponceList> getDate(NYTApi api) {
        return api.getMostEmailedDate(Constants.SECTION, Constants.TIME_PERIOD, Constants.API_KEY);
    }
}
