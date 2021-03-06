package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import cuidadosmayores.tfi.iturrizj.tfiandroid.BE.Trayectoria
import cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Callback
import cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Helpers.PreferencesHelper
import cuidadosmayores.tfi.iturrizj.tfiandroid.R
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Response


class MapsActivity : ActivityWithBackButton(), OnMapReadyCallback, GoogleMap.CancelableCallback {

    private lateinit var mMap: GoogleMap
    lateinit var bottomsheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var selectedPosition: Int = 0
    lateinit var trayectorias: List<Trayectoria>
    lateinit var ubicaciones: List<LatLng>

    var trayectoriasCallback = object : Callback<List<Trayectoria>>() {

        override fun onResponse(call: Call<List<Trayectoria>>?, response: Response<List<Trayectoria>>?) {
            if (response?.code() == 200) {
                trayectorias = response?.body() ?: ArrayList<Trayectoria>()
                super.onResponse(call, response)
            }
        }

        override fun onFailure(call: Call<List<Trayectoria>>?, t: Throwable?) {
            super.onFailure(call, t)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        bottomsheetBehavior = BottomSheetBehavior.from(bottomm_sheet)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (PreferencesHelper.isEnabled(applicationContext, PreferencesHelper.BooleanElements.SHOW_MAPS_TUTORIAL)) showTutorial()

        initializeBottomSheet()
        getMockUbicaciones()
        /* ServiceGenerator.createService(CuidadosMayoresServices::class.java)
                 ?.getTrayectorias()
                 ?.enqueue(trayectoriasCallback)*/
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMaxZoomPreference(19f)
        mMap.setMinZoomPreference(15f)
        val posicionActual = LatLng(-34.607985, -58.373510)
        mMap.addMarker(MarkerOptions().position(posicionActual).title("Posicion Actual"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionActual, 17f))
    }



    private fun initializeBottomSheet() {
        bottomsheetBehavior.isHideable = true
        bottomsheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        floating.setOnClickListener({ _ ->
            if (bottomsheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomsheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomsheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })

        radio2.setOnClickListener({ _ -> selectedPeriod(0) })
        radio12.setOnClickListener({ _ -> selectedPeriod(1) })
        radio24.setOnClickListener({ _ -> selectedPeriod(2) })
    }

    private fun selectedPeriod(index: Int) {
        selectedPosition = 0
        ubicaciones = trayectorias[index].ubicaciones
        textview_distancia.text = "Distancia recorrida : " + trayectorias[index].distanciaTotal.toString() + " metros"

        //Dibujo el polyline
        val rectOptions = PolylineOptions()
                .addAll(ubicaciones)
                .geodesic(true)
                .color(resources.getColor(R.color.colorAccent))
        mMap.addPolyline(rectOptions)

        //Animo el recorrido
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition()), 4000, this@MapsActivity)
        selectedPosition++
    }

    override fun onFinish() {
        if (selectedPosition < ubicaciones.size) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition()), 4000, this@MapsActivity)
            selectedPosition++
        }
    }

    override fun onCancel() {
    }

    private fun getCameraPosition(): CameraPosition {
        //Obtengo la Camara
        val bearing: Float = if (selectedPosition < ubicaciones.size - 1) getBearing(ubicaciones[selectedPosition], ubicaciones[selectedPosition + 1]) else 0f
        return CameraPosition.builder()
                .zoom(18.3f)
                .target(ubicaciones[selectedPosition])
                .tilt(90f)
                .bearing(bearing)
                .build()
    }

    private fun getBearing(desde: LatLng, hasta: LatLng): Float {
        //Calculo el angulo de camara para apuntar
        val beginLocation = Location("")
        beginLocation.latitude = desde.latitude
        beginLocation.longitude = desde.longitude
        val endLocation = Location("")
        endLocation.latitude = hasta.latitude
        endLocation.longitude = hasta.longitude
        return beginLocation.bearingTo(endLocation)
    }


    private fun getMockUbicaciones(){
        trayectorias = ArrayList<Trayectoria>()
        var trayectoria2 = Trayectoria()
        trayectoria2.distanciaTotal = 620
        trayectoria2.ubicaciones = arrayListOf<LatLng>(
                LatLng(-34.611561, -58.376246),
                LatLng(-34.608956, -58.373392),
                LatLng(-34.607985, -58.373510)
        )

        var trayectoria12 = Trayectoria()
        trayectoria12.distanciaTotal = 815
        trayectoria12.ubicaciones = arrayListOf<LatLng>(
                LatLng(-34.615412, -58.384500),
                LatLng(-34.615209, -58.379575),
                LatLng(-34.614979, -58.376013),
                LatLng(-34.611561, -58.376246),
                LatLng(-34.608956, -58.373392),
                LatLng(-34.607985, -58.373510)
        )

        var trayectoria24 = Trayectoria()
        trayectoria24.distanciaTotal = 1030
        trayectoria24.ubicaciones = arrayListOf<LatLng>(
                LatLng(-34.613584, -58.388284),
                LatLng(-34.613319, -58.384650),
                LatLng(-34.615412, -58.384500),
                LatLng(-34.615209, -58.379575),
                LatLng(-34.614979, -58.376013),
                LatLng(-34.611561, -58.376246),
                LatLng(-34.608956, -58.373392),
                LatLng(-34.607985, -58.373510)
        )


        trayectorias = arrayListOf<Trayectoria>(
                trayectoria2, trayectoria12, trayectoria24
        )

    }

    private fun showTutorial (){
        PreferencesHelper.setEnabled(applicationContext, PreferencesHelper.BooleanElements.SHOW_MAPS_TUTORIAL, false)
        startActivity(Intent(applicationContext, TutorialMaps::class.java))
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out)
    }

}