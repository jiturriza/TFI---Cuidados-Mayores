package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.app.Application
import com.facebook.FacebookSdk
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import cuidadosmayores.tfi.iturrizj.tfiandroid.UI.LoginActivity


class ApplicationCM : Application() {

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.setApplicationId("572767276391517")
        FacebookSdk.sdkInitialize(applicationContext)
    }

    fun logOut(){
        FirebaseAuth.getInstance().signOut()
        var intent = Intent(applicationContext, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }


}