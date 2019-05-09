package com.sargis.kh.guardian.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sargis.kh.guardian.R;
import com.sargis.kh.guardian.databinding.LayoutRecyclerViewItemPinnedBinding;
import com.sargis.kh.guardian.listeners.OnItemClickListener;
import com.sargis.kh.guardian.models.Results;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePagePinnedAdapter extends RecyclerView.Adapter<HomePagePinnedAdapter.DataAdapterViewHolder> {

    private final OnItemClickListener listener;

    private ArrayList<Results> results;

    public HomePagePinnedAdapter(OnItemClickListener listener) {
        this.listener = listener;
        this.results = new ArrayList<>();
    }

    @NonNull
    @Override
    public DataAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRecyclerViewItemPinnedBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_recycler_view_item_pinned,
                parent,false);

        return new DataAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapterViewHolder holder, int position) {
        holder.bindHomePageData(results.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(ArrayList<Results> results) {
//        ArrayList<Results> arrayList = new ArrayList(results);
        this.results.clear();
        this.results.addAll(results);

        notifyDataSetChanged();
    }

    public ArrayList<Results> getData() {
        return results;
    }

    public class DataAdapterViewHolder extends RecyclerView.ViewHolder {

        private LayoutRecyclerViewItemPinnedBinding binding;

        public DataAdapterViewHolder(LayoutRecyclerViewItemPinnedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindHomePageData(final Results result, OnItemClickListener listener) {

            if (result.fields != null && result.fields.thumbnail != null) {

                Picasso.get().load(result.fields.thumbnail)
//                        .resize(100, 100)
                        .placeholder(R.drawable.white_image)
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