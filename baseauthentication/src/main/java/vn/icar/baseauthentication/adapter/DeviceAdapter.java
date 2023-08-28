package vn.icar.baseauthentication.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.icar.baseauthentication.R;
import vn.icar.baseauthentication.data.model.Device;
import vn.icar.baseauthentication.listener.DeviceClick;


public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    ArrayList<Device> listDevice;
    Context context;
    DeviceClick onclick;

    public void DeviceClick(DeviceClick onclick) {
        this.onclick = onclick;
    }

    public DeviceAdapter(ArrayList<Device> listDevice, Context context) {
        this.listDevice = listDevice;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_limit, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtDevice.setText(listDevice.get(position).getDeviceName().equals("") ? "--/--" : listDevice.get(position).getDeviceName());

        holder.btDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDevice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDevice, btDevice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDevice = itemView.findViewById(R.id.txt_device);
            btDevice = itemView.findViewById(R.id.bt_device);
        }
    }
}