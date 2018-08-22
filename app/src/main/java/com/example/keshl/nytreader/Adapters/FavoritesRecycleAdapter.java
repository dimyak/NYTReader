package com.example.keshl.nytreader.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keshl.nytreader.Constants;
import com.example.keshl.nytreader.OnSavedRecycleListener;
import com.example.keshl.nytreader.R;
import com.example.keshl.nytreader.activitys.ArticleActivity;
import com.example.keshl.nytreader.model.ArticleDbModel;

import java.util.List;

public class FavoritesRecycleAdapter extends RecyclerView.Adapter<FavoritesRecycleAdapter.ViewHolder> {

    private List<ArticleDbModel> resultsItems;
    private Context context;
    private OnSavedRecycleListener onSavedRecycleListener;

    public FavoritesRecycleAdapter(List<ArticleDbModel> resultsItems, Context context, OnSavedRecycleListener onSavedRecycleListener) {
        this.resultsItems = resultsItems;
        this.context = context;
        this.onSavedRecycleListener = onSavedRecycleListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(context).inflate(R.layout.recycler_save_element, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        final int adapterPosition = holder.getAdapterPosition();
        holder.title.setText(resultsItems.get(adapterPosition).getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra(Constants.ARTICLE_ACTIVITY_CASE,Constants.OPEN_DOWNLOAD_ARTICLE);
                intent.putExtra(Constants.ARTICLE_TITLE,resultsItems.get(adapterPosition).getTitle());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSavedRecycleListener.deleteItem(adapterPosition);
                Toast.makeText(context,"Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private CardView cardView;
        private ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitleSaveElement);
            cardView = itemView.findViewById(R.id.cardViewElement);
            btnDelete = itemView.findViewById(R.id.imageButtonDelete);

        }
    }
}
