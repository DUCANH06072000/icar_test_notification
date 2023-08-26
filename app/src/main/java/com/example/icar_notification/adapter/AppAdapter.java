package com.example.icar_notification.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.icar_notification.R;
import com.example.icar_notification.databinding.ItemAppInDeviceBinding;
import com.example.icar_notification.listener.SelectAppListener;
import com.example.icar_notification.model.AppDevice;
import com.tatv.baseapp.adapter.BaseAdapter;

import java.util.List;

public class AppAdapter extends BaseAdapter<ItemAppInDeviceBinding, AppDevice, SelectAppListener> {

    public AppAdapter(Context context, List<AppDevice> list, SelectAppListener listener) {
        super(context, list, listener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_app_in_device;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter<ItemAppInDeviceBinding, AppDevice, SelectAppListener>.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.txtItemAppDevice.setText(list.get(position).getNameApp());
        holder.binding.appIconImageView.setImageDrawable(list.get(position).getDrawableApp());
        holder.binding.txtItemAppDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectAppListener(list.get(position));
            }
        });
    }
}
