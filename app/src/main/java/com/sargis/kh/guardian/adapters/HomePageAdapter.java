package com.sargis.kh.guardian.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sargis.kh.guardian.R;
import com.sargis.kh.guardian.databinding.LayoutRecyclerViewItemBinding;
import com.sargis.kh.guardian.listeners.OnBottomReachedListener;
import com.sargis.kh.guardian.models.Results;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.DataAdapterViewHolder> {

    private OnBottomReachedListener onBottomReachedListener;
    private List<Results> results;
    private Context context;

    public HomePageAdapter(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
        this.results = new ArrayList<>();
    }

    @NonNull
    @Override
    public DataAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutRecyclerViewItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_recycler_view_item,
                parent,false);

        return new DataAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapterViewHolder holder, int position) {
        holder.bindHomePageData(results.get(position));
        if (position == results.size() - 3){
            onBottomReachedListener.onBottomReached(position);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Results> results) {
        ArrayList<Results> arrayList = new ArrayList(results);
        this.results.addAll(arrayList);

        notifyDataSetChanged();
    }

    public class DataAdapterViewHolder extends RecyclerView.ViewHolder {

        private LayoutRecyclerViewItemBinding binding;

        public DataAdapterViewHolder(LayoutRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            binding.setOnItemClick(v -> removeItem(getAdapterPosition()));
        }

        public void bindHomePageData(final Results result) {

            Picasso.get().load(getCoverImageResourceIdByPillarName(result.sectionName))
                    .resize(80, 80)
                    .centerCrop()
                    .transform(new CropCircleTransformation())
                    .into(binding.imageView);

            binding.setCategory(result.sectionName);
            binding.setTitle(result.webTitle);

            itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.webUrl));
                    context.startActivity(intent);
            });
        }

        // NOTE
        // In free version it is not possible to get articles' image, that is why I just added local images to decorate the list
        private int getCoverImageResourceIdByPillarName(String pillarName) {

            if (pillarName != null)
                pillarName = pillarName.toLowerCase();
            else
                pillarName = "";

            switch (pillarName) {
                case "arts":
                    return R.drawable.arts;
                case "news":
                    return R.drawable.news;
                case "sport":
                    return R.drawable.sport;
                case "opinion":
                    return R.drawable.opinion;
                case "lifestyle":
                    return R.drawable.lifestyle;
                default:
                    return R.drawable.unknown;
            }
        }


    }
}