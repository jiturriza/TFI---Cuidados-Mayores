package cuidadosmayores.tfi.iturrizj.tfiandroid.SmartBand

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.widget.FrameLayout
import cuidadosmayores.tfi.iturrizj.tfiandroid.ApplicationCM
import cuidadosmayores.tfi.iturrizj.tfiandroid.R

class ConfigDeviceActivity : FragmentActivity() , ClaimDeviceFragment.OnClaimedDeviceListener {

    lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_device)
        frameLayout = findViewById(R.id.frame_layout)

        val app: ApplicationCM = application as ApplicationCM
        if (app.dispositivoVinculado) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, ScanDevicekFragment.newInstance())
                    .commitAllowingStateLoss()

        } else {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, ClaimDeviceFragment.newInstance())
                    .commitAllowingStateLoss()
        }

    }

    override fun onClaimedDevice() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }
}
