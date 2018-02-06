package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import cuidadosmayores.tfi.iturrizj.tfiandroid.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : ActivityWithBackButton(), onItemClick {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var settingsItems: List<SettingsItemAdapter.SettingsItem> = arrayListOf(
                SettingsItemAdapter.SettingsItem("Ver tutoriales", "Seleccione para volver a ver los tutoriales.", android.R.drawable.ic_dialog_email),
                SettingsItemAdapter.SettingsItem("Cerrar Sesión", "Cerrar Sesion con su cuenta actual", android.R.drawable.ic_dialog_email)
        ).toList()

        recyclerview.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recyclerview.adapter = SettingsItemAdapter(applicationContext, settingsItems, this@SettingsActivity)
    }

    override fun onClick(position: Int) {
        //ver tutoriales
        when (position) {
            0 -> { //Tutoriales
                PreferencesHelper.setEnabled(applicationContext, PreferencesHelper.BooleanElements.SHOW_TUTORIAL, false)
                Toast.makeText(applicationContext, "Tutoriales restaurados", Toast.LENGTH_SHORT).show()
            }
            1 -> (application as ApplicationCM).logOut() //Cerrar sesion
        }

    }
}

interface onItemClick {
    fun onClick(position: Int)
}
