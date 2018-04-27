package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.app.Application
import com.facebook.FacebookSdk
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.facebook.login.LoginManager
import com.google.android.gms.fitness.data.BleDevice
import cuidadosmayores.tfi.iturrizj.tfiandroid.UI.LoginActivity


class ApplicationCM : Application() {

    var device: BleDevice? = null

    var dispositivoVinculado : Boolean = false
        get() = field
        set(value) {
            field = value
        }

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.setApplicationId("572767276391517")
        FacebookSdk.sdkInitialize(applicationContext)
    }

    fun logOut() {
        FirebaseAuth.getInstance().signOut() //Logout de Firebase
        LoginManager.getInstance().logOut() //Logout de Facebook
        var intent = Intent(applicationContext, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }


}