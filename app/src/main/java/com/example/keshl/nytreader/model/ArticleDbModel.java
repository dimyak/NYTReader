package com.example.keshl.nytreader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleDbModel implements Parcelable {
    private String title;
    private String html;

    public ArticleDbModel(String title, String html){
        this.title = title;
        this.html = html;
    }

    private ArticleDbModel(Parcel parcel) {
        title = parcel.readString();
        html = parcel.readString();
    }


    public String getTitle() {
        return title;
    }

    public String getHtml() {
        return html;
    }

    public static final Parcelable.Creator<ArticleDbModel> CREATOR = new Parcelable.Creator<ArticleDbModel>() {

        public ArticleDbModel createFromParcel(Parcel in) {
            return new ArticleDbModel(in);
        }

        public ArticleDbModel[] newArray(int size) {
            return new ArticleDbModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(html);
    }
}
