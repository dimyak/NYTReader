package com.example.keshl.nytreader.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponceList {

    @SerializedName("copyright")
    private String copyright;

    @SerializedName("results")
    private List<ResultsItem> results;

    @SerializedName("num_results")
    private int numResults;

    @SerializedName("status")
    private String status;

    public void setCopyright(String copyright){
        this.copyright = copyright;
    }

    public String getCopyright(){
        return copyright;
    }

    public void setResults(List<ResultsItem> results){
        this.results = results;
    }

    public List<ResultsItem> getResults(){
        return results;
    }

    public void setNumResults(int numResults){
        this.numResults = numResults;
    }

    public int getNumResults(){
        return numResults;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return
                "ResponseList{" +
                        "copyright = '" + copyright + '\'' +
                        ",results = '" + results + '\'' +
                        ",num_results = '" + numResults + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }

}
