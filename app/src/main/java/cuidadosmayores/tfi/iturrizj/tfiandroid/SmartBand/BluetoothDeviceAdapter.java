package cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand;

import android.bluetooth.BluetoothClass;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.fitness.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

import cuidadosmayores.tfi.iturrizj.tfiandroid.R;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder> {

    private List<BleDevice> deviceList = new ArrayList<>();
    private OnSelectedDeviceListener listener;

    public BluetoothDeviceAdapter(OnSelectedDeviceListener listener) {
        this.listener = listener;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bluetooth_device, parent, false);
        return new DeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, final int position) {
        final BleDevice device = deviceList.get(position);
        holder.deviceNameTextView.setText(device.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnSelectedDevice(deviceList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {

        public TextView deviceNameTextView;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            deviceNameTextView = itemView.findViewById(R.id.device_name);
        }

    }

    public void addDevice(BleDevice device) {
        if (!yaAgregado(device)){
            this.deviceList.add(device);
            notifyDataSetChanged();
        }
    }

    private boolean yaAgregado(BleDevice p){
        for (BleDevice d: deviceList) {
            if (p.getName() == null) return  true;
            if (p.getAddress().equals(d.getAddress())) return true;
        }
        return false;
    }

}
