package com.example.keshl.nytreader.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.keshl.nytreader.Constants;
import com.example.keshl.nytreader.R;
import com.example.keshl.nytreader.activitys.ArticleActivity;
import com.example.keshl.nytreader.model.ResultsItem;

import java.util.List;

public class MostPopularRecyclerAdapter extends RecyclerView.Adapter<MostPopularRecyclerAdapter.ViewHolder> {

    private List<ResultsItem> resultsItems;
    private Context context;

    public MostPopularRecyclerAdapter(List<ResultsItem> resultsItems, Context context) {
        this.resultsItems = resultsItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.most_popular_recycler_elemet, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(resultsItems.get(position).getTitle());
        holder.publishedDate.setText(resultsItems.get(position).getPublishedDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra(Constants.ARTICLE_ACTIVITY_CASE, Constants.OPEN_ARTICLE);
                intent.putExtra(Constants.ARTICLE_URL, resultsItems.get(position).getUrl());
                intent.putExtra(Constants.ARTICLE_TITLE, resultsItems.get(position).getTitle());
                context.startActivity(intent);
            }
        });

        if (resultsItems.get(position).getMedia() != null &&
                resultsItems.get(position).getMedia().size() > 0 &&
                resultsItems.get(position).getMedia().get(0).getMediaMetadata().size() > 0) {
            String urlImage = resultsItems.get(position).getMedia().get(0).getMediaMetadata().get(0).getUrl();
            Glide.with(context)
                    .asBitmap()
                    .load(urlImage)
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return resultsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, publishedDate;
        private ImageView imageView;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitleElement);
            publishedDate = itemView.findViewById(R.id.textViewPublishedDateElement);
            imageView = itemView.findViewById(R.id.imageViewElement);
            cardView = itemView.findViewById(R.id.cardViewElement);

        }
    }
}
