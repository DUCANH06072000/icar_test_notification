package com.tatv.baseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<V extends ViewDataBinding, T, I> extends RecyclerView.Adapter<BaseAdapter<V, T, I>.ViewHolder> {
    protected Context context;
    protected List<T> list;
    protected I listener;

    public BaseAdapter(Context context, List<T> list, I listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        V binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), parent, false);
        return new ViewHolder(binding);
    }

    protected abstract int getLayoutId();


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public V binding;
        public ViewHolder(V binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
