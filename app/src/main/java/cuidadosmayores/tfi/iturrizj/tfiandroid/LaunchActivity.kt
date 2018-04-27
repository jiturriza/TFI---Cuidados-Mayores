package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.Manifest
import android.app.Activity
import android.app.VoiceInteractor
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.BleScanCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand.ConfigDeviceActivity
import cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand.Log
import cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand.ScanActivity
import cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand.SensorActivity
import cuidadosmayores.tfi.iturrizj.tfiandroid.UI.LoginActivity
import cuidadosmayores.tfi.iturrizj.tfiandroid.UI.MainActivity
import java.util.*

class LaunchActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    lateinit var mApiClient: GoogleApiClient
    private val REQUEST_OAUTH_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        mApiClient = GoogleApiClient.Builder(this@LaunchActivity)
                .enableAutoManage(this@LaunchActivity, this)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.BLE_API)
                .addScope(Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .build()

        //connectar con cuenta Google
        mApiClient.connect()

    }

    private fun isDispositivoVinculado() {

        val app: ApplicationCM = application as ApplicationCM

        var dispositivosVinculadosListener: OnCompleteListener<List<BleDevice>> = object : OnCompleteListener<List<BleDevice>> {
            override fun onComplete(p0: Task<List<BleDevice>>) {
                if (p0.isSuccessful) {
                    for (bleDevice in p0.result) {
                        if (bleDevice.getDataTypes().contains(DataType.TYPE_HEART_RATE_BPM)) {
                            app.device = bleDevice
                            app.dispositivoVinculado = true
                        }
                    }
                }
                startActivity(Intent(this@LaunchActivity, ConfigDeviceActivity::class.java))
            }
        }

        Fitness.getBleClient(this, GoogleSignIn.getLastSignedInAccount(this)).listClaimedBleDevices()
                .addOnCompleteListener(dispositivosVinculadosListener)

    }

    //region interfaces de conexion

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this@LaunchActivity, "Fallo", Toast.LENGTH_LONG).show()
    }

    override fun onConnected(p0: Bundle?) {
//        isDispositivoVinculado()
        requestOAuthPermission()
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(this@LaunchActivity, "Fallo", Toast.LENGTH_LONG).show()
    }

    //endregion

    private fun requestOAuthPermission() {
        val fitnessOptions = getFitnessSignInOptions()
        GoogleSignIn.requestPermissions(
                this,
                REQUEST_OAUTH_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this@LaunchActivity),
                fitnessOptions)
    }

    private fun getFitnessSignInOptions(): FitnessOptions {
        return FitnessOptions.builder().addDataType(DataType.TYPE_LOCATION_SAMPLE).build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                isDispositivoVinculado()
            }
        }
    }


}