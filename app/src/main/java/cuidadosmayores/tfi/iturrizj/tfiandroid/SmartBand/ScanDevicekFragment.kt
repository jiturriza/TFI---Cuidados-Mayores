package cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.DataType.TYPE_HEART_RATE_BPM
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

import cuidadosmayores.tfi.iturrizj.tfiandroid.R
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScanDevicekFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScanDevicekFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanDevicekFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private val dataType = TYPE_HEART_RATE_BPM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fitness.getBleClient(requireActivity(), GoogleSignIn.getLastSignedInAccount(requireContext()))
                .listClaimedBleDevices().addOnSuccessListener(dispositivosVinculadosListener)
    }


    internal var dispositivosVinculadosListener: OnSuccessListener<List<BleDevice>> = OnSuccessListener { bleDevices ->
        var deviceFound = false
        for (bleDevice in bleDevices) {
            if (bleDevice.dataTypes.contains(dataType)) {
                deviceFound = true
                Toast.makeText(requireContext(), "Ya estaba vinculado, se autovinculó", Toast.LENGTH_SHORT).show()
                Fitness.getBleClient(requireActivity(), GoogleSignIn.getLastSignedInAccount(requireContext()))
                        .claimBleDevice(bleDevice)
                        .addOnCompleteListener(claimDeviceListener)
            }
        }
    }

    internal var claimDeviceListener: OnCompleteListener<Void> = OnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(requireContext(), "Dispositivo vinculado con éxito", Toast.LENGTH_SHORT).show()
            addDataPoint()
        } else {
            Toast.makeText(requireContext(), "Dispositivo no se pudo vincular", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addDataPoint() {

        Fitness.getSensorsClient(requireActivity(), GoogleSignIn.getLastSignedInAccount(requireContext()))
                .findDataSources(DataSourcesRequest.Builder()
                        .setDataSourceTypes(DataSource.TYPE_RAW)
                        .setDataTypes(dataType)
                        .build())
                .addOnSuccessListener { dataSources ->
                    for (dataSource in dataSources) {
                        if (dataSource.dataType == dataType) {
                            registerFitnessDataListener(dataSource, dataType)
                        }
                    }
                }
    }


    private fun registerFitnessDataListener(dataSource: DataSource, dataType: DataType) {
        var mListener = OnDataPointListener { dataPoint ->
            for (field in dataPoint.dataType.fields) {
                val `val` = dataPoint.getValue(field)
                Toast.makeText(requireContext(), "DATO!!!!!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show()
            }
        }

        Fitness.getSensorsClient(requireActivity(), GoogleSignIn.getLastSignedInAccount((activity as ConfigDeviceActivity).application))
                .add(
                        SensorRequest.Builder()
                                .setDataSource(dataSource) // Optional but recommended for custom data sets.
                                .setDataType(dataType) // Can't be omitted.
                                .setSamplingRate(15, TimeUnit.SECONDS)
                                .setTimeout(80, TimeUnit.SECONDS)
                                .setAccuracyMode(1)
                                .build(),
                        mListener)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Listener registered!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Listener not registered!", Toast.LENGTH_SHORT).show()
                    }
                }

        Fitness.RecordingApi.subscribe(null, null).

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan_devicek, container, false)
    }

    /* override fun onAttach(context: Context?) {
         super.onAttach(context)
         if (context is OnFragmentInteractionListener) {
             mListener = context
         } else {
             throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
         }
     }
 */
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        fun newInstance(): ScanDevicekFragment {
            val fragment = ScanDevicekFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
