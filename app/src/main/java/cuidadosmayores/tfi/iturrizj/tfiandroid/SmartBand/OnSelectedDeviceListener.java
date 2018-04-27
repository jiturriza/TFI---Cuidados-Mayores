package cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand;

import android.bluetooth.BluetoothDevice;

import com.google.android.gms.fitness.data.BleDevice;

public interface OnSelectedDeviceListener {

    public void OnSelectedDevice(BleDevice device);

}
