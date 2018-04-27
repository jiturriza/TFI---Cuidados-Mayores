package cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Device;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.BleScanCallback;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.request.StartBleScanRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cuidadosmayores.tfi.iturrizj.tfiandroid.R;
import cuidadosmayores.tfi.iturrizj.tfiandroid.UI.LoginActivity;

import static com.google.android.gms.fitness.data.DataType.TYPE_HEART_RATE_BPM;

public class ScanActivity extends AppCompatActivity implements OnSelectedDeviceListener {

    private RecyclerView recyclerView;
    private BluetoothDeviceAdapter adapter;
    private static final int REQUEST_BLUETOOTH_CODE = 859;
    private static final String TAG = LoginActivity.class.getName();
    private OnDataPointListener mListener;
    private DataType dataType = TYPE_HEART_RATE_BPM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bluetooth_devices);
        pedirPermisos();
        /*recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BluetoothDeviceAdapter(this);
        recyclerView.setAdapter(adapter);*/

        initializeLogging();

        if (isBluetoothActive()) {

            //Veo si ya hay dispositivos vinculados
            Fitness.getBleClient(this, GoogleSignIn.getLastSignedInAccount(this)).
                    listClaimedBleDevices().
                    addOnSuccessListener(dispositivosVinculadosListener);

/*            ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().startLeScan(new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    adapter.addDevice(device);
                }
            }); */

        } else {
            Toast.makeText(this, "Habilite Bluetooth por favor", Toast.LENGTH_SHORT).show();
        }

    }

    //region Primer intento

    //region Inicializar Listeners
    OnSuccessListener<List<BleDevice>> dispositivosVinculadosListener = new OnSuccessListener<List<BleDevice>>() {
        @Override
        public void onSuccess(List<BleDevice> bleDevices) {
            boolean deviceFound = false;
            for (BleDevice bleDevice : bleDevices) {
                if (bleDevice.getDataTypes().contains(dataType)) {
                    deviceFound = true;
                    Toast.makeText(ScanActivity.this, "Ya estaba vinculado, se autovinculó", Toast.LENGTH_SHORT).show();
                    Fitness.getBleClient(ScanActivity.this, GoogleSignIn.getLastSignedInAccount(getApplicationContext()))
                            .claimBleDevice(bleDevice)
                            .addOnCompleteListener(claimDeviceListener);
                }
            }

            //Si no se vinculo ninguno se listan los disponibles
            if (!deviceFound)
                Fitness.getBleClient(ScanActivity.this, GoogleSignIn.getLastSignedInAccount(getApplicationContext()))
                        .startBleScan(Arrays.asList(dataType), 1000, bleScanCallbacks);
        }
    };

    BleScanCallback bleScanCallbacks = new BleScanCallback() {
        @Override
        public void onDeviceFound(BleDevice device) {
            // A device that provides the requested data types is available
//            adapter.addDevice(device);
        }

        @Override
        public void onScanStopped() {
            // The scan timed out or was interrupted
        }
    };

    OnCompleteListener<Void> claimDeviceListener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Dispositivo vinculado con éxito", Toast.LENGTH_SHORT).show();
                addDataPoint();
            } else {
                Toast.makeText(getApplicationContext(), "Dispositivo no se pudo vincular", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //endregion

    //region Pedir permisos Bluetooth

    private void pedirPermisos() {
        int permissionCheck = ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.BLUETOOTH_ADMIN);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_BLUETOOTH_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_BLUETOOTH_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ScanActivity.this, "Bluetooth acceso permitido", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
    //endregion

    private boolean isBluetoothActive() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();
    }

    @Override
    public void OnSelectedDevice(final BleDevice device) {
//        Fitness.getBleClient(this, GoogleSignIn.getLastSignedInAccount(this)).claimBleDevice(device).addOnSuccessListener(claimDeviceListener);
    }
//endregion

    private void addDataPoint() {

        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(getApplicationContext()))
                .findDataSources(new DataSourcesRequest.Builder()
                        .setDataSourceTypes(DataSource.TYPE_RAW)
                        .setDataTypes(dataType)
                        .build())
                .addOnSuccessListener(new OnSuccessListener<List<DataSource>>() {
                    @Override
                    public void onSuccess(List<DataSource> dataSources) {
                        for (DataSource dataSource : dataSources) {
                            if (dataSource.getDataType().equals(dataType)) {
                                Log.i(TAG, "Data source found: " + dataSource.toString());
                                Log.i(TAG, "Data Source type: " + dataSource.getDataType().getName());
                                Log.i(TAG, "Data source for TYPE_HEART_RATE_BPM found!  Registering.");
                                registerFitnessDataListener(dataSource, dataType);
                            }
                        }
                    }
                });
        ;
    }

    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
        mListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (Field field : dataPoint.getDataType().getFields()) {
                    Value val = dataPoint.getValue(field);
                    Log.i(TAG, "Detected DataPoint field: " + field.getName());
                    Log.i(TAG, "Detected DataPoint value: " + val);
                }
            }
        };

        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .add(
                        new SensorRequest.Builder()
                                .setDataSource(dataSource) // Optional but recommended for custom data sets.
                                .setDataType(dataType) // Can't be omitted.
                                .setSamplingRate(2, TimeUnit.SECONDS)
                                .setTimeout(10, TimeUnit.SECONDS)
                                .build(),
                        mListener)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Listener registered!");
                                } else {
                                    Log.e(TAG, "Listener not registered.", task.getException());
                                }
                            }
                        });
    }

    private void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);
        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);
        // On screen logging via a customized TextView.
        LogView logView = (LogView) findViewById(R.id.sample_logview);

        logView.setBackgroundColor(Color.WHITE);
        msgFilter.setNext(logView);
        Log.i(TAG, "Ready");
    }

    private FitnessOptions getFitnessSignInOptions() {
        return FitnessOptions.builder().addDataType(dataType).build();
    }
}
