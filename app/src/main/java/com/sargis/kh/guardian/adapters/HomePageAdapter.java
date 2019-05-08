package com.sargis.kh.guardian.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sargis.kh.guardian.R;
import com.sargis.kh.guardian.databinding.LayoutRecyclerViewItemBinding;
import com.sargis.kh.guardian.listeners.OnBottomReachedListener;
import com.sargis.kh.guardian.listeners.OnItemClickListener;
import com.sargis.kh.guardian.models.Results;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.DataAdapterViewHolder> {

    private OnBottomReachedListener onBottomReachedListener;
    private final OnItemClickListener listener;

    private List<Results> results;

    public HomePageAdapter(OnBottomReachedListener onBottomReachedListener, OnItemClickListener listener) {
        this.onBottomReachedListener = onBottomReachedListener;
        this.listener = listener;
        this.results = new ArrayList<>();
    }

    @NonNull
    @Override
    public DataAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRecyclerViewItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_recycler_view_item,
                parent,false);

        return new DataAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapterViewHolder holder, int position) {
        holder.bindHomePageData(results.get(position), listener);
        if (position == results.size() - 3){
            onBottomReachedListener.onBottomReached(position);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void addNewResults(List<Results> results) {
        ArrayList<Results> arrayList = new ArrayList(results);
        this.results.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void setResults(List<Results> results) {
        ArrayList<Results> arrayList = new ArrayList(results);
        this.results.clear();
        this.results.addAll(arrayList);
        notifyDataSetChanged();
    }

    public class DataAdapterViewHolder extends RecyclerView.ViewHolder {

        private LayoutRecyclerViewItemBinding binding;

        public DataAdapterViewHolder(LayoutRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindHomePageData(final Results result, OnItemClickListener listener) {

            if (result.fields != null && result.fields.thumbnail != null) {

                Picasso.get().load(result.fields.thumbnail)
                        .placeholder(R.drawable.white_image)

//                        .memoryPolicy(MemoryPolicy.NO_CACHE )
                        .networkPolicy(NetworkPolicy.NO_CACHE)

//                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(binding.imageView);
            } else {
                Picasso.get().load(R.drawable.white_image)
                        .into(binding.imageView);
            }

            binding.setCategory(result.sectionName);
            binding.setTitle(result.webTitle);

            binding.setOnItemClick(v -> {
                listener.onItemClick(result);
            });
        }


    }
}