package com.moutamid.peptidesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.peptidesapp.activities.FavoriteActivity;
import com.moutamid.peptidesapp.model.ProductModel;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteVH> {

    Context context;
    ArrayList<ProductModel> list;
    FavoriteActivity.ClickListener clickListener;

    public FavoritesAdapter(Context context, ArrayList<ProductModel> list, FavoriteActivity.ClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FavoriteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteVH(LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteVH holder, int position) {
        ProductModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.getName());
        holder.doseInfo.setText(model.getName());
        holder.more.setText(model.getLongDesc());
        Glide.with(context).load(model.getImage()).placeholder(R.color.white).into(holder.imageView);
        holder.delete.setOnClickListener(v -> clickListener.onClick(holder.getAdapterPosition()));

        holder.view_more.setOnClickListener(v -> {
            int vis = holder.more.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            holder.more.setVisibility(vis);
        });

        String longDesc = model.getLongDesc();
        String link = model.getLink() != null ? model.getLink() : context.getString(R.string.website);
        String combined = longDesc.concat("\n\n").concat(link);
        SpannableString longString = new SpannableString(combined);
        int startIndex = Math.min(longDesc.length(), combined.length());
        int endIndex = combined.length();
        longString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.green)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        longString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.more.setText(longString);
        holder.more.setOnClickListener(v -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link))));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavoriteVH extends RecyclerView.ViewHolder{
        TextView name, doseInfo, view_more, more;
        ImageView imageView;
        MaterialCardView delete;
        public FavoriteVH(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            imageView = itemView.findViewById(R.id.imageView);
            view_more = itemView.findViewById(R.id.viewMore);
            doseInfo = itemView.findViewById(R.id.doseInfo);
            name = itemView.findViewById(R.id.name);
            more = itemView.findViewById(R.id.more);
        }
    }

}
