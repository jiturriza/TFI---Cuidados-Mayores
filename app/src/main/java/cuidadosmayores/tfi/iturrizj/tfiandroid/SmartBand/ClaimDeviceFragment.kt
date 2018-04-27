package cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import cuidadosmayores.tfi.iturrizj.tfiandroid.R
import kotlinx.android.synthetic.main.fragment_claim_device.*
import java.util.*
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.fitness.request.BleScanCallback


class ClaimDeviceFragment : Fragment(), OnSelectedDeviceListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val REQUEST_BLUETOOTH_CODE = 859
    private var mListener: OnClaimedDeviceListener? = null
    private val adapter: BluetoothDeviceAdapter = BluetoothDeviceAdapter(this)
    lateinit var mApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pedirPermisos()
        if (isBluetoothActive()) {

            mApiClient = GoogleApiClient.Builder(requireContext())
                    .enableAutoManage(activity as FragmentActivity, this)
                    .addApi(Fitness.SENSORS_API)
                    .addApi(Fitness.BLE_API)
                    .addScope(Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()

            //connecting
            mApiClient.connect()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.setLayoutManager(LinearLayoutManager(requireContext()));
        recyclerview.adapter = adapter

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_claim_device, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnClaimedDeviceListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnClaimedDeviceListener {
        fun onClaimedDevice()
    }

    companion object {
        fun newInstance(): ClaimDeviceFragment {
            return ClaimDeviceFragment()
        }
    }

    override fun OnSelectedDevice(device: BleDevice) {
        Fitness.BleApi.claimBleDevice(mApiClient, device).setResultCallback { status ->
            if (status.isSuccess) Toast.makeText(requireContext(), "Vinculaidsimoooo!!", Toast.LENGTH_LONG).show()
        }
    }

    //region Interfaces de Conexion
    override fun onConnected(p0: Bundle?) {
        Fitness.getBleClient(requireContext(), GoogleSignIn.getLastSignedInAccount(requireContext()))
                .startBleScan(Arrays.asList(DataType.TYPE_HEART_RATE_BPM), 1000,
                        object : BleScanCallback() {
                            override fun onScanStopped() {
                            }

                            override fun onDeviceFound(p0: BleDevice?) {
                                adapter.addDevice(p0);
                            }
                        })
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(requireContext(), "Fallo", Toast.LENGTH_LONG).show()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(requireContext(), "Fallo", Toast.LENGTH_LONG).show()
    }
    //endregion


    //region Pedir permisos Bluetooth

    private fun pedirPermisos() {
        val permissionCheck = ContextCompat.checkSelfPermission(this@ClaimDeviceFragment.requireContext(), Manifest.permission.BLUETOOTH_ADMIN)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@ClaimDeviceFragment.requireActivity(),
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_BLUETOOTH_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_BLUETOOTH_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@ClaimDeviceFragment.requireContext(), "Bluetooth acceso permitido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isBluetoothActive(): Boolean {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return mBluetoothAdapter.isEnabled
    }


    //endregion

}
